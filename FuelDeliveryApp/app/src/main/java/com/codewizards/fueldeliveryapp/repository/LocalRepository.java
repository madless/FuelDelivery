package com.codewizards.fueldeliveryapp.repository;

import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.entities.Order;
import com.codewizards.fueldeliveryapp.utils.Logger;

import java.util.List;

import rx.Observable;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class LocalRepository implements IRepository {
    private Logger logger = Logger.getLogger(this.getClass());
    private Observable<List<Delivery>> deliveries = Observable.empty();
    private Observable<List<City>> cities = Observable.empty();

    @Override
    public Observable<List<Delivery>> getDeliveries() {
        logger.d("getDeliveries()");
        return deliveries;
    }

    @Override
    public Observable<List<City>> getCities() {
        logger.d("getCities()");
        return cities;
    }

    @Override
    public Observable<List<Order>> getOrdersByDeliveryId(final int deliveryId) {
        logger.d("getOrdersByDeliveryId: " + deliveryId);
        return deliveries.flatMap(fetchedDeliveries -> {
            List<Order> orders = null;
            for (Delivery delivery: fetchedDeliveries) {
                if(delivery.getId() == deliveryId) {
                    orders = delivery.getOrders();
                    break;
                }
            }
            if(orders == null) {
                logger.error("Orders == null");
            }
            return Observable.just(orders);
        });
    }

    public void setDeliveries(Observable<List<Delivery>> deliveries) {
        logger.d("deliveries obs saved!");
        this.deliveries = deliveries;
    }

    public void setCities(Observable<List<City>> cities) {
        logger.d("cities obs saved!");
        this.cities = cities;
    }
}
