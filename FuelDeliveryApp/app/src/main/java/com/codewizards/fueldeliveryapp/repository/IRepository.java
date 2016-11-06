package com.codewizards.fueldeliveryapp.repository;

import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.entities.Order;

import java.util.List;

import rx.Observable;

/**
 * Created by dmikhov on 21.10.2016.
 */
public interface IRepository {
    Observable<List<Delivery>> getDeliveries();
    Observable<List<City>> getCities();
    Observable<List<Order>> getOrdersByDeliveryId(int deliveryId);
    Observable<List<Delivery>> addDelivery(Delivery delivery);
}
