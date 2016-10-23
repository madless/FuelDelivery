package com.codewizards.fueldeliveryapp.ui.delivery.tab_map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Coordinates;
import com.codewizards.fueldeliveryapp.entities.Order;
import com.codewizards.fueldeliveryapp.ui.delivery.BaseTabFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class DeliveryMapFragment extends BaseTabFragment implements OnMapReadyCallback {

    private GoogleMap mMap;

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
    public void onMapReady(GoogleMap googleMap) {
        logger.d("onMapReady");
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        if(activity != null && activity.getDelivery() != null && activity.getDelivery().getOrders() != null) {
            List<Order> orders = activity.getDelivery().getOrders();
            for (Order order: orders) {
                if(order != null && order.getCity() != null && order.getCity().getCoordinates() != null) {
                    City city = order.getCity();
                    Coordinates location = order.getCity().getCoordinates();
                    LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(coordinates).title(city.getName()));
                }

            }
        }
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}
