package com.codewizards.fueldeliveryapp.repository;

import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.entities.Order;
import com.codewizards.fueldeliveryapp.net.FuelDeliveryMockServer;
import com.codewizards.fueldeliveryapp.utils.Logger;

import java.util.List;

import rx.Observable;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class CloudRepository implements IRepository {
    protected Logger logger = Logger.getLogger(this.getClass());
    private UpdateListener updateListener;

    @Override
    public Observable<List<Delivery>> getDeliveries() {
        logger.d("getDeliveries()");
//        Observable<List<Delivery>> deliveries = FuelDeliveryHerokuServer.getApi().getDeliveries();
        Observable<List<Delivery>> deliveries = FuelDeliveryMockServer.getApi().getDeliveries();
        if(updateListener != null) {
            updateListener.updateDeliveries(deliveries);
        }
        return deliveries;
    }

    @Override
    public Observable<List<City>> getCities() {
        logger.d("getCities()");
//        Observable<List<City>> cities = FuelDeliveryHerokuServer.getApi().getCities();
        Observable<List<City>> cities = FuelDeliveryMockServer.getApi().getCities();
        if(updateListener != null) {
            updateListener.updateCities(cities);
        }
        return cities;
    }

    @Override
    public Observable<List<Order>> getOrdersByDeliveryId(int deliveryId) {
        throw new RuntimeException("Not implemented!");
    }

    public void setUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }
}
