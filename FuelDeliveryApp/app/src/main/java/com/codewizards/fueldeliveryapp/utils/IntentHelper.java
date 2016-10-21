package com.codewizards.fueldeliveryapp.utils;

import android.app.Activity;
import android.content.Intent;

import com.codewizards.fueldeliveryapp.ui.delivery.DeliveryActivity;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class IntentHelper {
    public void openDeliveryActivity(Activity activity, int deliveryId) {
        Intent intent = new Intent(activity, DeliveryActivity.class);
        intent.putExtra(Const.EXTRA_DELIVERY_ID, deliveryId);
        activity.startActivity(intent);
    }
}
