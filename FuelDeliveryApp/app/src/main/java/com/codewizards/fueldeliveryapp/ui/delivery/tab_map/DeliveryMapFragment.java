package com.codewizards.fueldeliveryapp.ui.delivery.tab_map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.ui.delivery.BaseTabFragment;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class DeliveryMapFragment extends BaseTabFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_delivery_map, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
