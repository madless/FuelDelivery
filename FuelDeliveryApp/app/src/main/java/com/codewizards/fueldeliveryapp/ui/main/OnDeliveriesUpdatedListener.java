package com.codewizards.fueldeliveryapp.ui.main;

import com.codewizards.fueldeliveryapp.entities.Delivery;

import java.util.List;

/**
 * Created by madless on 06.11.2016.
 */
public interface OnDeliveriesUpdatedListener {
    void onDeliveriesUpdated(List<Delivery> deliveries);
}
