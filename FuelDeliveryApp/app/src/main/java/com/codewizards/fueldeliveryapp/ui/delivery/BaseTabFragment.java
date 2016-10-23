package com.codewizards.fueldeliveryapp.ui.delivery;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codewizards.fueldeliveryapp.ui.abs.BaseFragment;

/**
 * Created by dmikhov on 21.10.2016.
 */
public abstract class BaseTabFragment extends BaseFragment {
    protected DeliveryActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (DeliveryActivity) getActivity();
    }


}
