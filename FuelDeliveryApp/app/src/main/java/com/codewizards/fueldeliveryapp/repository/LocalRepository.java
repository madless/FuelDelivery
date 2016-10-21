package com.codewizards.fueldeliveryapp.repository;

import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.entities.Order;
import com.codewizards.fueldeliveryapp.utils.Logger;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class LocalRepository implements IRepository {
    private Logger logger = Logger.getLogger(this.getClass());
    private Observable<List<Delivery>> deliveries;
    private Observable<List<City>> cities;

    @Override
    public Observable<List<Delivery>> getDeliveries() {
        return deliveries;
    }

    @Override
    public Observable<List<City>> getCities() {
        return cities;
    }

    @Override
    public Observable<List<Order>> getOrdersByDeliveryId(final int deliveryId) {
        return deliveries.flatMap(new Func1<List<Delivery>, Observable<List<Order>>>() {
                    @Override
                    public Observable<List<Order>> call(List<Delivery> deliveries) {
                        List<Order> orders = null;
                        for (Delivery delivery: deliveries) {
                            if(delivery.getId() == deliveryId) {
                                orders = delivery.getOrders();
                                break;
                            }
                        }
                        if(orders == null) {
                            logger.error("Orders == null");
                        }
                        return Observable.just(orders);
                    }
                });
    }

    public void setDeliveries(Observable<List<Delivery>> deliveries) {
        this.deliveries = deliveries;
    }

    public void setCities(Observable<List<City>> cities) {
        this.cities = cities;
    }
}
