package com.codewizards.fueldeliveryapp.ui.delivery.tab_map;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Coordinates;
import com.codewizards.fueldeliveryapp.entities.Order;
import com.codewizards.fueldeliveryapp.ui.delivery.BaseTabFragment;
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

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class DeliveryMapFragment extends BaseTabFragment implements OnMapReadyCallback {

    private static final int CAMERA_ZOOM = 5;
    private static final int CAMERA_TILT = 0;
    private static final int CAMERA_SPEED = 1000;
    private static final int START_DELAY = 1500;

    private GoogleMap map;
    private int index;
    private int pathIndex;
    private Polyline polyline;
    private PolylineOptions rectOptions = new PolylineOptions();
    private List<Marker> markers = new ArrayList<>();
    private Marker selectedMarker;
    private Handler handler = new Handler();
    private PathAnimator pathAnimator = new PathAnimator();
    private Marker trackingMarker;
    private Graph seaGraph;
    private List<Path> seaPathes = new ArrayList<>();
    private Order firstOrder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_delivery_map, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        InputStream is = getResources().openRawResource(R.raw.sea);
        DijkstraXmlParser parser = new DijkstraXmlParser();
        try {
            seaGraph = parser.getGraph(is);
//            seaGraph.print();
            List<Order> orders = getOrders();
            firstOrder = orders.get(0);
            DijkstraCalc dijkstraCalc = new DijkstraCalc();
            for (int i = 0; i < orders.size() - 1; i++) {
                Order orderFrom = orders.get(i);
                Order orderTo = orders.get(i + 1);
                List<Vertex> shortestPath = dijkstraCalc.getShortestPath(seaGraph, orderFrom.getCity().getCoordinates(), orderTo.getCity().getCoordinates());
                logger.w("shortestPath: " + shortestPath);
                seaPathes.add(new Path(shortestPath));
            }
            mapFragment.getMapAsync(this);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        if(activity != null && activity.getDelivery() != null && activity.getDelivery().getOrders() != null) {
            List<Order> orders = activity.getDelivery().getOrders();
            for (Order order: orders) {
                if(order != null && order.getCity() != null && order.getCity().getCoordinates() != null) {
                    City city = order.getCity();
                    Coordinates location = city.getCoordinates();
                    LatLng coordinates = new LatLng(location.getLat(), location.getLon());
                    Marker marker = map.addMarker(new MarkerOptions().position(coordinates).title(city.getName()));
                    markers.add(marker);
                }
            }
            handler.postDelayed(() -> {
                City city = firstOrder.getCity();
                Coordinates location = city.getCoordinates();
                LatLng coordinates = new LatLng(location.getLat(), location.getLon());
                trackingMarker = map.addMarker(new MarkerOptions()
                        .position(coordinates)
                        .title("Ship")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_ship_small))
                );
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
            }, START_DELAY);
        }
    }

    public List<Vertex> getCurrentPath() {
        return seaPathes.get(pathIndex).getVertices();
    }

    GoogleMap.CancelableCallback simpleAnimationCancelableCallback =
            new GoogleMap.CancelableCallback(){
                @Override
                public void onCancel() {
                }
                @Override
                public void onFinish() {
                    pathAnimator.start();
                }
            };


    public List<Order> getOrders() {
        return activity.getDelivery().getOrders();
    }

    private void updatePolyLine(LatLng latLng) {
        List<LatLng> points = polyline.getPoints();
        points.add(latLng);
        polyline.setPoints(points);
    }

    public class PathAnimator implements Runnable {
        private static final int ANIMATE_SPEEED = 1000;
        private static final int STEP_DELAY = 15;

        LatLng endLatLng = null;
        LatLng beginLatLng = null;
        private final Interpolator interpolator = new LinearInterpolator();
        long start = SystemClock.uptimeMillis();

        public PathAnimator() {

        }

        private LatLng getEndLatLng() {
            Coordinates coordinates = getCurrentPath().get(index + 1).getCoordinates();
            return new LatLng(coordinates.getLat(), coordinates.getLon());
        }

        private LatLng getBeginLatLng() {
            Coordinates coordinates = getCurrentPath().get(index).getCoordinates();
            return new LatLng(coordinates.getLat(), coordinates.getLon());
        }

        private void update() {
            start = SystemClock.uptimeMillis();
            endLatLng = getEndLatLng();
            beginLatLng = getBeginLatLng();
        }

        public void start() {
            if(index < getCurrentPath().size() - 1) {
                update();
            }
            handler.postDelayed(this, STEP_DELAY);
        }

        private void stop() {
            handler.removeCallbacks(null);
        }

        @Override
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - start;
            double t = interpolator.getInterpolation((float)elapsed / ANIMATE_SPEEED);
            double lat = t * endLatLng.latitude + (1-t) * beginLatLng.latitude;
            double lng = t * endLatLng.longitude + (1-t) * beginLatLng.longitude;
            LatLng newPosition = new LatLng(lat, lng);

            trackingMarker.setPosition(newPosition);
            updatePolyLine(newPosition);

            if (t < 1) {
                // move path
                handler.postDelayed(this, STEP_DELAY);
            } else {
                logger.d("t >= 0, index: " + index + ", markers.size(): " + markers.size());
                if(index < getCurrentPath().size() - 2) {
                    index++;
                    Coordinates c = getCurrentPath().get(index).getCoordinates();
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
                        start();
                    }
                }

            }
        }
    }
}
