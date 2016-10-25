package com.codewizards.fueldeliveryapp.net;

import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Coordinates;
import com.codewizards.fueldeliveryapp.entities.Delivery;
import com.codewizards.fueldeliveryapp.entities.FuzzyNumber;
import com.codewizards.fueldeliveryapp.entities.Order;
import com.codewizards.fueldeliveryapp.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Body;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class FuelDeliveryMockImpl implements FuelDeliveryApi {
    Logger logger = Logger.getLogger(this.getClass());
    List<City> cities = new ArrayList<>();
    List<Delivery> deliveries = new ArrayList<>();
    BaseResponse response;

    public FuelDeliveryMockImpl() {
        init();
    }

    public void init() {
        Coordinates cd1 = new Coordinates(53.3441, -6.2675);
        Coordinates cd2 = new Coordinates(46.482526, 30.723310);
        Coordinates cd3 = new Coordinates(41.008238, 28.978359);
        Coordinates cd4 = new Coordinates(40.7127837, -74.0059413);
        Coordinates cd5 = new Coordinates(46.975033, 31.994583);
        City city1 = new City(0, "Dublin", cd1);
        City city2 = new City(1, "Odessa", cd2);
        City city3 = new City(2, "Istanbul", cd3);
        City city4 = new City(3, "New York", cd4);
        City city5 = new City(4, "Nikolaev", cd5);
        FuzzyNumber fn1 = new FuzzyNumber(100, 100, 200);
        FuzzyNumber fn2 = new FuzzyNumber(100, 150, 250);
        FuzzyNumber fn3 = new FuzzyNumber(90, 160, 170);
        FuzzyNumber fn4 = new FuzzyNumber(50, 70, 120);
        FuzzyNumber fn5 = new FuzzyNumber(100, 120, 170);
        Order o1 = new Order(0, city1, fn1);
        Order o2 = new Order(1, city2, fn2);
        Order o3 = new Order(2, city3, fn3);
        Order o4 = new Order(3, city4, fn4);
        Order o5 = new Order(4, city5, fn5);
        List<Order> orders1 = new ArrayList<>();
        List<Order> orders2 = new ArrayList<>();
        orders1.add(o1); // 1
        orders1.add(o3); // 3
        orders1.add(o2); // 2
        orders1.add(o5); // 5
        orders2.add(o4);
        Delivery d1 = new Delivery(0, "Sea delivery 1", 700, orders1);
        Delivery d2 = new Delivery(1, "Sea delivery 2", 300, orders2);
        deliveries.add(d1);
        deliveries.add(d2);
        cities.add(city1);
        cities.add(city2);
        cities.add(city3);
        cities.add(city4);
        cities.add(city5);
        response = new BaseResponse(200);
    }

    @Override
    public Observable<List<Delivery>> getDeliveries() {
        return Observable.create(new Observable.OnSubscribe<List<Delivery>>() {
            @Override
            public void call(Subscriber<? super List<Delivery>> subscriber) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(deliveries);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<City>> getCities() {
        return Observable.create(new Observable.OnSubscribe<List<City>>() {
            @Override
            public void call(Subscriber<? super List<City>> subscriber) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(cities);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<BaseResponse> addDelivery(@Body Delivery delivery) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                deliveries.add(delivery);
                subscriber.onNext(response);
                subscriber.onCompleted();
            }
        });
    }
}
