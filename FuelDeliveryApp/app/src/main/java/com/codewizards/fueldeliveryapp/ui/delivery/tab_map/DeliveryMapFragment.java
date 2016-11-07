package com.codewizards.fueldeliveryapp.ui.delivery.tab_map;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Coordinates;
import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.entities.Order;
import com.codewizards.fueldeliveryapp.ui.delivery.BaseTabFragment;
import com.codewizards.fueldeliveryapp.utils.calculator.FuzzyNumberHelper;
import com.codewizards.fueldeliveryapp.utils.dijkstra.DijkstraCalc;
import com.codewizards.fueldeliveryapp.utils.dijkstra.DijkstraXmlParser;
import com.codewizards.fueldeliveryapp.utils.dijkstra.entities.Graph;
import com.codewizards.fueldeliveryapp.utils.dijkstra.entities.Path;
import com.codewizards.fueldeliveryapp.utils.dijkstra.entities.Vertex;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class DeliveryMapFragment extends BaseTabFragment implements OnMapReadyCallback {

    private static final int CAMERA_ZOOM = 5;
    private static final int CAMERA_TILT = 0;
    private static final int CAMERA_SPEED = 1000;
    private static final int START_DELAY = 1000;
    private static final int SHIP_ANIMATE_SPEED = 1000;
    private static final int SHIP_STEP_DELAY = 5;
    private static final int IN_CITY_DELAY = 1000;
    private static final String SHIP_ROUTE_FORMAT = "%s -> %s";
    private static final String SHIP_FUEL_FORMAT = "Fuel: %s";

    @Bind(R.id.fabStartDelivery) FloatingActionButton fabDelivery;

    private GoogleMap map;
    private IconGenerator iconFactory;
    private PathAnimator pathAnimator;
    private Interpolator interpolator;
    private Handler handler;
    private Polyline polyline;
    private PolylineOptions rectOptions;
    private List<Marker> markers;
    private Marker trackingMarker;
    private Graph seaGraph;
    private List<Path> seaPathes;
    List<Order> orders = new ArrayList<>();
    private int index;
    private int pathIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iconFactory = new IconGenerator(getContext());
        pathAnimator = new PathAnimator();
        interpolator = new LinearInterpolator();
        handler = new Handler();
        rectOptions = new PolylineOptions();
        markers = new ArrayList<>();
        seaPathes = new ArrayList<>();
        iconFactory.setStyle(R.style.CityInfoText);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_delivery_map, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabDelivery.setOnClickListener(view1 -> {
            resetData();
            initPathsData();
            fillMathWithOrderMarkers();
            startMoving();
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        fillMathWithOrderMarkers();
    }

    public void startMoving() {
        if(map != null) {
            map.getUiSettings().setScrollGesturesEnabled(false);
            City city = getDelivery().getSourceCity();
            Coordinates location = city.getCoordinates();
            LatLng coordinates = new LatLng(location.getLat(), location.getLon());
            trackingMarker = map.addMarker(new MarkerOptions()
                    .position(coordinates)
                    .title(String.format(SHIP_ROUTE_FORMAT, getCurrentPath().getFrom().getName(), getCurrentPath().getTo().getName()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_ship_small))
                    .snippet(String.format(SHIP_FUEL_FORMAT, getDelivery().getAmountOfFuel().toString()))
            );
            trackingMarker.showInfoWindow();
            polyline = map.addPolyline(rectOptions);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(coordinates)
                    .tilt(CAMERA_TILT)
                    .zoom(CAMERA_ZOOM)
                    .build();
            map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(cameraPosition),
                    CAMERA_SPEED,
                    simpleAnimationCancelableCallback);
        } else {
            Toast.makeText(getContext(), "Map is not ready yet!", Toast.LENGTH_SHORT).show();
        }
    }

    public void resetData() {
        orders.clear();
        index = 0;
        pathIndex = 0;
        seaPathes.clear();
        markers.clear();
        if(map != null) {
            map.clear();
        } else {
            logger.w("resetData(), map is null");
        }
    }

    public void fillMathWithOrderMarkers() {
        if(map != null && orders != null) {
            LatLng srcLocation = new LatLng(getDelivery().getSourceCity().getCoordinates().getLat(), getDelivery().getSourceCity().getCoordinates().getLon());
            Marker srcMarker = map.addMarker(new MarkerOptions()
                    .position(srcLocation)
                    .title(getDelivery().getSourceCity().getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
            );
            markers.add(srcMarker);
            for (Order order: orders) {
                if(order != null && order.getCity() != null && order.getCity().getCoordinates() != null) {
                    City city = order.getCity();
                    LatLng location = new LatLng(city.getCoordinates().getLat(), city.getCoordinates().getLon());
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(location)
                            .title(city.getName())
                            .snippet(String.format(SHIP_FUEL_FORMAT, order.getAmountOfFuel().toString()))
                    );
                    String name = city.getName();
                    String fuel = order.getAmountOfFuel().toString();
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(name + ": " + fuel)));
                    markers.add(marker);
                }
            }
        }
    }

    public void initPathsData() {
        InputStream is = getResources().openRawResource(R.raw.sea);
        DijkstraXmlParser parser = new DijkstraXmlParser();
        try {
            seaGraph = parser.getGraph(is);
            orders.addAll(getOrders());
            for (int i = 0; i < orders.size(); i++) {
                if(!FuzzyNumberHelper.isFuzzyValid(orders.get(i).getAmountOfFuelAfterOrder())) {
                    orders.remove(i);
                    i--;
                }
            }
            List<City> cities = new ArrayList<>(orders.size() + 1);
            cities.add(getDelivery().getSourceCity());
            for (Order order: orders) {
                cities.add(order.getCity());
            }
            DijkstraCalc dijkstraCalc = new DijkstraCalc();
            for (int i = 0; i < cities.size() - 1; i++) {
                City from = cities.get(i);
                City to = cities.get(i + 1);
                List<Vertex> shortestPath = dijkstraCalc.getShortestPath(seaGraph, from.getCoordinates(), to.getCoordinates());
                logger.d("Shortest path: " + shortestPath);
                seaPathes.add(new Path(from, to, shortestPath));
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTrackingMarkerInfo() {
        int orderIndex = pathIndex - 1;
        logger.d("order index: " + orderIndex + ", orders: " + orders.size() + ", " + orders);
        if(orders != null && orders.size() > orderIndex) {
            if(orderIndex != orders.size() - 1) { // not last
                trackingMarker.setTitle(String.format(SHIP_ROUTE_FORMAT, getCurrentPath().getFrom().getName(), getCurrentPath().getTo().getName()));
            } else {
                trackingMarker.setTitle("Delivered!");
            }
            trackingMarker.setSnippet(String.format(SHIP_FUEL_FORMAT, orders.get(orderIndex).getAmountOfFuelAfterOrder()));
            trackingMarker.showInfoWindow();
        } else {
            logger.w("Some orders or index issues!");
        }

    }

    public void updateCityMarker() {
        markers.get(pathIndex).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ok_circle_bottom));
    }

    public void updateLastCityMarker() {
        if(pathIndex < seaPathes.size() - 1) {
            markers.get(markers.size() - 1).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ok_circle_bottom));
            Coordinates c = getCurrentPathVertexes().get(getCurrentPathVertexes().size() - 1).getCoordinates();
            LatLng location = new LatLng(c.getLat(), c.getLon());
            CameraPosition cameraPosition =
                    new CameraPosition.Builder()
                            .target(location)
                            .tilt(0)
                            .zoom(CAMERA_ZOOM)
                            .build();
            map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(cameraPosition),
                    CAMERA_SPEED,
                    null);
        }
    }

    public List<Vertex> getCurrentPathVertexes() {
        return seaPathes.get(pathIndex).getVertices();
    }

    public Path getCurrentPath() {
        return seaPathes.get(pathIndex);
    }

    GoogleMap.CancelableCallback simpleAnimationCancelableCallback =
            new GoogleMap.CancelableCallback(){
                @Override
                public void onCancel() {}
                @Override
                public void onFinish() {
                    pathAnimator.update();
                    pathAnimator.start();
                }
            };


    public List<Order> getOrders() {
        return activity.getDelivery().getOrders();
    }

    public Delivery getDelivery() {
        return activity.getDelivery();
    }

    private void updatePolyLine(LatLng latLng) {
        List<LatLng> points = polyline.getPoints();
        points.add(latLng);
        polyline.setPoints(points);
    }

    public class PathAnimator implements Runnable {

        private LatLng endLatLng = null;
        private LatLng beginLatLng = null;
        private long start;

        private LatLng getEndLatLng() {
            Coordinates coordinates = getCurrentPathVertexes().get(index + 1).getCoordinates();
            return new LatLng(coordinates.getLat(), coordinates.getLon());
        }

        private LatLng getBeginLatLng() {
            Coordinates coordinates = getCurrentPathVertexes().get(index).getCoordinates();
            return new LatLng(coordinates.getLat(), coordinates.getLon());
        }

        private void update() {
            if(index < getCurrentPathVertexes().size() - 1) {
                start = SystemClock.uptimeMillis();
                endLatLng = getEndLatLng();
                beginLatLng = getBeginLatLng();
            }
        }

        public void start() {
            handler.postDelayed(this, SHIP_STEP_DELAY);
        }

        private void stop() {
            handler.removeCallbacks(null);
        }

        @Override
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - start;
            double t = interpolator.getInterpolation((float)elapsed / SHIP_ANIMATE_SPEED);
            double lat = t * endLatLng.latitude + (1-t) * beginLatLng.latitude;
            double lng = t * endLatLng.longitude + (1-t) * beginLatLng.longitude;
            LatLng newPosition = new LatLng(lat, lng);

            trackingMarker.setPosition(newPosition);
            updatePolyLine(newPosition);

            if (t < 1) {
                // move path
                start();
            } else {
//                logger.d("t >= 0, index: " + index);
                if(index < getCurrentPathVertexes().size() - 2) {
                    index++;
                    Coordinates c = getCurrentPathVertexes().get(index).getCoordinates();
                    LatLng location = new LatLng(c.getLat(), c.getLon());
                    CameraPosition cameraPosition =
                            new CameraPosition.Builder()
                                    .target(location)
                                    .tilt(0)
                                    .zoom(CAMERA_ZOOM)
                                    .build();
                    map.animateCamera(
                            CameraUpdateFactory.newCameraPosition(cameraPosition),
                            CAMERA_SPEED,
                            simpleAnimationCancelableCallback);
                } else {
                    stop();
                    if(pathIndex < seaPathes.size() - 1) {
                        pathIndex++;
                        index = 0;
                        updateTrackingMarkerInfo();
                        updateCityMarker();
                        handler.postDelayed(() -> {
                            update();
                            start();
                        }, IN_CITY_DELAY);
                    } else {
                        pathIndex++;
                        logger.i("Route passed! pathIndex: " + pathIndex + ", seaPathes.size(): " + seaPathes.size());
                        updateLastCityMarker();
                        updateTrackingMarkerInfo();
                        map.getUiSettings().setScrollGesturesEnabled(true); // enable map moves by tapping
                    }
                }

            }
        }
    }
}
