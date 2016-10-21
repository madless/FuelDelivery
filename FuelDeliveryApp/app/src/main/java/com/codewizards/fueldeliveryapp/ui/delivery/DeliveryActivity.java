package com.codewizards.fueldeliveryapp.ui.delivery;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codewizards.fueldeliveryapp.R;
import com.codewizards.fueldeliveryapp.ui.abs.BaseActivity;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class DeliveryActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
    }
}
