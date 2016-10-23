package com.codewizards.fueldeliveryapp.repository;

import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.entities.Order;
import com.codewizards.fueldeliveryapp.net.BaseResponse;
import com.codewizards.fueldeliveryapp.utils.Logger;

import java.util.List;
import java.util.NoSuchElementException;

import rx.Observable;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class RepositoryManager implements IRepository, UpdateListener {
    protected Logger logger = Logger.getLogger(this.getClass());
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
        logger.d("getDeliveries()");
        Observable<List<Delivery>> localObservable = localRepository.getDeliveries();
        Observable<List<Delivery>> cloudObservable = cloudRepository.getDeliveries();
        try {
            return localObservable.switchIfEmpty(cloudObservable);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Observable<List<City>> getCities() {
        logger.d("getCities()");
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
        logger.d("updateDeliveries");
        localRepository.setDeliveries(deliveries);
    }

    @Override
    public void updateCities(Observable<List<City>> cities) {
        logger.d("updateCities");
        localRepository.setCities(cities);
    }

    @Override
    public Observable<BaseResponse> addDelivery(Delivery delivery) {
        return cloudRepository.addDelivery(delivery);
    }
}
