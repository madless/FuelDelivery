package com.codewizards.fueldeliveryapp.repository;

import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.entities.Order;

import java.util.List;
import java.util.NoSuchElementException;

import rx.Observable;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class RepositoryManager implements IRepository, UpdateListener {
    private CloudRepository cloudRepository;
    private LocalRepository localRepository;
    private static RepositoryManager instance = new RepositoryManager();
    private RepositoryManager() {
        localRepository = new LocalRepository();
        cloudRepository = new CloudRepository();
        cloudRepository.setUpdateListener(this);
    }
    public static RepositoryManager get() {
        return instance;
    }

    @Override
    public Observable<List<Delivery>> getDeliveries() {
        Observable<List<Delivery>> localObservable = localRepository.getDeliveries();
        Observable<List<Delivery>> cloudObservable = cloudRepository.getDeliveries();
        try {
            return Observable.concat(localObservable, cloudObservable).first();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Observable<List<City>> getCities() {
        Observable<List<City>> localObservable = localRepository.getCities();
        Observable<List<City>> cloudObservable = cloudRepository.getCities();
        try {
            return Observable.concat(localObservable, cloudObservable).first();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Observable<List<Order>> getOrdersByDeliveryId(int deliveryId) {
        Observable<List<Order>> localObservable = localRepository.getOrdersByDeliveryId(deliveryId);
        Observable<List<Order>> cloudObservable = cloudRepository.getOrdersByDeliveryId(deliveryId);
        try {
            return Observable.concat(localObservable, cloudObservable).first();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateDeliveries(Observable<List<Delivery>> deliveries) {
        localRepository.setDeliveries(deliveries);
    }

    @Override
    public void updateCities(Observable<List<City>> cities) {
        localRepository.setCities(cities);
    }
}
