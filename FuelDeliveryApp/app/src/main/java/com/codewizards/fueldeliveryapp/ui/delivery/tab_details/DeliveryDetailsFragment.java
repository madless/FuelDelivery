package com.codewizards.fueldeliveryapp.ui.delivery.tab_details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.ui.delivery.BaseTabFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class DeliveryDetailsFragment extends BaseTabFragment {

    @Bind(R.id.tvDeliveryName) TextView tvDeliveryName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logger.d("onCreateView");
        View root = inflater.inflate(R.layout.fragment_delivery_details, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logger.d("onViewCreated(), delivery: " + delivery);
        if(delivery != null) {
            tvDeliveryName.setText(delivery.getName());
        }
    }
}
