package com.codewizards.fueldeliveryapp.repository;

import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Delivery;

import java.util.List;

import rx.Observable;

/**
 * Created by dmikhov on 21.10.2016.
 */
public interface UpdateListener {
    void updateDeliveries(Observable<List<Delivery>> deliveries);
    void updateCities(Observable<List<City>> cities);
}
