package com.codewizards.fueldeliveryapp.ui.delivery.tab_map;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Coordinates;
import com.codewizards.fueldeliveryapp.entities.Order;
import com.codewizards.fueldeliveryapp.ui.delivery.BaseTabFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class DeliveryMapFragment extends BaseTabFragment implements OnMapReadyCallback {

    private static final int CAMERA_ZOOM = 9;
    private static final int CAMERA_TILT = 0;
    private static final int CAMERA_SPEED = 3000;

    private GoogleMap map;
    private int index;
    private Polyline polyline;
    private PolylineOptions rectOptions = new PolylineOptions();
    private List<Marker> markers = new ArrayList<>();
    private Marker selectedMarker;
    private Handler handler = new Handler();
//    private Animator animator = new Animator();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_delivery_map, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logger.d("onViewCreated");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        logger.d("onMapReady");
        this.map = map;
        // Add a marker in Sydney and move the camera
        if(activity != null && activity.getDelivery() != null && activity.getDelivery().getOrders() != null) {
            List<Order> orders = activity.getDelivery().getOrders();
            for (Order order: orders) {
                if(order != null && order.getCity() != null && order.getCity().getCoordinates() != null) {
                    City city = order.getCity();
                    Coordinates location = order.getCity().getCoordinates();
                    LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                    Marker marker = map.addMarker(new MarkerOptions().position(coordinates).title(city.getName()));
                    markers.add(marker);
                }

            }
            polyline = map.addPolyline(rectOptions);
//            animator.startAnimation(true);
            Coordinates c =  getOrders().get(index).getCity().getCoordinates();
            LatLng location = new LatLng(c.getLatitude(), c.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(location)
                            .tilt(index < getOrders().size() - 1 ? 0 : 0)
                            //.bearing((float)heading)
                            .zoom(CAMERA_ZOOM)
                            .build();
            map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(cameraPosition),
                    CAMERA_SPEED,
                    simpleAnimationCancelableCallback);
        }
//        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    GoogleMap.CancelableCallback simpleAnimationCancelableCallback =
            new GoogleMap.CancelableCallback(){
                @Override
                public void onCancel() {
                }
                @Override
                public void onFinish() {
                    if(++index < getOrders().size()){
                        Coordinates c =  getOrders().get(index).getCity().getCoordinates();
                        LatLng location = new LatLng(c.getLatitude(), c.getLongitude());
                        CameraPosition cameraPosition =
                                new CameraPosition.Builder()
                                        .target(location)
                                        .tilt(index < getOrders().size() - 1 ? 90 : 0)
                                        //.bearing((float)heading)
                                        .zoom(map.getCameraPosition().zoom)
                                        .build();
                        map.animateCamera(
                                CameraUpdateFactory.newCameraPosition(cameraPosition),
                                CAMERA_SPEED,
                                simpleAnimationCancelableCallback);
//                        highLightMarker(currentPt);

                    }
                }
            };


    public List<Order> getOrders() {
        return activity.getDelivery().getOrders();
    }

//    public class Animator implements Runnable {
//
//        private static final int ANIMATE_SPEEED = 1500;
//        private static final int ANIMATE_SPEEED_TURN = 1000;
//        private static final int BEARING_OFFSET = 20;
//
//        private final Interpolator interpolator = new LinearInterpolator();
//
//        int currentIndex = 0;
//
//        float tilt = 0;
//        float zoom = 15.5f;
//        boolean upward = true;
//
//        long start = SystemClock.uptimeMillis();
//
//        LatLng endLatLng = null;
//        LatLng beginLatLng = null;
//
//        boolean showPolyline = false;
//
//        private Marker trackingMarker;
//
//        public void reset() {
//            resetMarkers();
//            start = SystemClock.uptimeMillis();
//            currentIndex = 0;
//            endLatLng = getEndLatLng();
//            beginLatLng = getBeginLatLng();
//
//        }
//
//        public void stop() {
//            trackingMarker.remove();
//            handler.removeCallbacks(animator);
//
//        }
//
//        public void initialize(boolean showPolyLine) {
//            reset();
//            this.showPolyline = showPolyLine;
//
//            highLightMarker(0);
//
//            if (showPolyLine) {
//                polyLine = initializePolyLine();
//            }
//
//            // We first need to put the camera in the correct position for the first run (we need 2 markers for this).....
//            LatLng markerPos = markers.get(0).getPosition();
//            LatLng secondPos = markers.get(1).getPosition();
//
//            setupCameraPositionForMovement(markerPos, secondPos);
//
//        }
//
//        private void setupCameraPositionForMovement(LatLng markerPos,
//                                                    LatLng secondPos) {
//
//            float bearing = bearingBetweenLatLngs(markerPos,secondPos);
//
//            trackingMarker = map.addMarker(new MarkerOptions().position(markerPos)
//                    .title("title")
//                    .snippet("snippet"));
//
//            CameraPosition cameraPosition =
//                    new CameraPosition.Builder()
//                            .target(markerPos)
//                            .bearing(bearing + BEARING_OFFSET)
//                            .tilt(0)
//                            .zoom(CAMERA_ZOOM)
//                            .build();
//
//            map.animateCamera(
//                    CameraUpdateFactory.newCameraPosition(cameraPosition),
//                    ANIMATE_SPEEED_TURN,
//                    new GoogleMap.CancelableCallback() {
//                        @Override
//                        public void onFinish() {
//                            System.out.println("finished camera");
//                            animator.reset();
//                            Handler handler = new Handler();
//                            handler.post(animator);
//                        }
//                        @Override
//                        public void onCancel() {
//                            System.out.println("cancelling camera");
//                        }
//                    }
//            );
//        }
//
//        private Polyline polyLine;
//        private PolylineOptions rectOptions = new PolylineOptions();
//
//
//        private Polyline initializePolyLine() {
//            //polyLinePoints = new ArrayList<LatLng>();
//            rectOptions.add(markers.get(0).getPosition());
//            return map.addPolyline(rectOptions);
//        }
//
//        /**
//         * Add the marker to the polyline.
//         */
//        private void updatePolyLine(LatLng latLng) {
//            List<LatLng> points = polyLine.getPoints();
//            points.add(latLng);
//            polyLine.setPoints(points);
//        }
//
//
//        public void stopAnimation() {
//            animator.stop();
//        }
//
//        public void startAnimation(boolean showPolyLine) {
//            if (markers.size() > 2) {
//                animator.initialize(showPolyLine);
//            }
//        }
//
//        @Override
//        public void run() {
//            long elapsed = SystemClock.uptimeMillis() - start;
//            double t = interpolator.getInterpolation((float)elapsed / ANIMATE_SPEEED);
//            double lat = t * endLatLng.latitude + (1-t) * beginLatLng.latitude;
//            double lng = t * endLatLng.longitude + (1-t) * beginLatLng.longitude;
//            LatLng newPosition = new LatLng(lat, lng);
//
//            trackingMarker.setPosition(newPosition);
//
//            if (showPolyline) {
//                updatePolyLine(newPosition);
//            }
//
//            if (t < 1) {
//                handler.postDelayed(this, 16);
//            } else {
//                System.out.println("Move to next marker.... current = " + currentIndex + " and size = " + markers.size());
//                // imagine 5 elements -  0|1|2|3|4 currentindex must be smaller than 4
//                if (currentIndex < markers.size() - 2) {
//
//                    currentIndex++;
//
//                    endLatLng = getEndLatLng();
//                    beginLatLng = getBeginLatLng();
//
//
//                    start = SystemClock.uptimeMillis();
//
//                    LatLng begin = getBeginLatLng();
//                    LatLng end = getEndLatLng();
//
//                    float bearingL = bearingBetweenLatLngs(begin, end);
//
//                    highLightMarker(currentIndex);
//
//                    CameraPosition cameraPosition =
//                            new CameraPosition.Builder()
//                                    .target(end) // changed this...
//                                    .bearing(bearingL  + BEARING_OFFSET)
//                                    .tilt(tilt)
//                                    .zoom(map.getCameraPosition().zoom)
//                                    .build();
//
//
//                    map.animateCamera(
//                            CameraUpdateFactory.newCameraPosition(cameraPosition),
//                            ANIMATE_SPEEED_TURN,
//                            null
//                    );
//
//                    start = SystemClock.uptimeMillis();
//                    handler.postDelayed(animator, 16);
//
//                } else {
//                    currentIndex++;
//                    highLightMarker(currentIndex);
//                    stopAnimation();
//                }
//
//            }
//        }
//
//        private LatLng getEndLatLng() {
//            return markers.get(currentIndex + 1).getPosition();
//        }
//
//        private LatLng getBeginLatLng() {
//            return markers.get(currentIndex).getPosition();
//        }
//
//    }
//
//    private Location convertLatLngToLocation(LatLng latLng) {
//        Location loc = new Location("someLoc");
//        loc.setLatitude(latLng.latitude);
//        loc.setLongitude(latLng.longitude);
//        return loc;
//    }
//
//    private float bearingBetweenLatLngs(LatLng begin,LatLng end) {
//        Location beginL= convertLatLngToLocation(begin);
//        Location endL= convertLatLngToLocation(end);
//
//        return beginL.bearingTo(endL);
//    }
//
//    /**
//     * Highlight the marker by index.
//     */
//    private void highLightMarker(int index) {
//        highLightMarker(markers.get(index));
//    }
//
//    /**
//     * Highlight the marker by marker.
//     */
//    private void highLightMarker(Marker marker) {
//        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//        marker.showInfoWindow();
//        this.selectedMarker = marker;
//    }
//
//    private void resetMarkers() {
//        for (Marker marker : this.markers) {
//            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        }
//    }

}
