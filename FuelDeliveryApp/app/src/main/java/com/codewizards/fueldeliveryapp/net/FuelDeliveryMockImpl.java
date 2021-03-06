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
        Coordinates cd6 = new Coordinates(45.440847, 12.315515);
        Coordinates cd7 = new Coordinates(44.405650, 8.946256);
        Coordinates cd8 = new Coordinates(37.983810, 23.727539);
        Coordinates cd9 = new Coordinates(37.793382, 12.431841);
        Coordinates cd10 = new Coordinates(37.034917, 27.433435);
        City city1 = new City(0, "Dublin", cd1);
        City city2 = new City(1, "Odessa", cd2);
        City city3 = new City(2, "Istanbul", cd3);
        City city4 = new City(3, "New York", cd4);
        City city5 = new City(4, "Nikolaev", cd5);
        City city6 = new City(5, "Venezia", cd6);
        City city7 = new City(6, "Genova", cd7);
        City city8 = new City(7, "Athens", cd8);
        City city9 = new City(8, "Marsala", cd9);
        City city10 = new City(9, "Bodrum", cd10);
        FuzzyNumber fn1 = new FuzzyNumber(100, 100, 200);
        FuzzyNumber fn2 = new FuzzyNumber(100, 150, 250);
        FuzzyNumber fn3 = new FuzzyNumber(90, 160, 170);
        FuzzyNumber fn4 = new FuzzyNumber(50, 70, 120);
        FuzzyNumber fn5 = new FuzzyNumber(100, 120, 170);
        FuzzyNumber fn6 = new FuzzyNumber(30, 50, 100);
        FuzzyNumber fn7 = new FuzzyNumber(100, 120, 130);
        FuzzyNumber fn8 = new FuzzyNumber(100, 150, 200);
//        Order o1 = new Order(0, city1, fn1);
        Order o2 = new Order(1, city2, fn2);
        Order o3 = new Order(2, city3, fn3);
        Order o4 = new Order(3, city4, fn4);
        Order o5 = new Order(4, city5, fn5);
        Order o6 = new Order(5, city6, fn6);
        Order o7 = new Order(6, city7, fn7);
        Order o8 = new Order(7, city8, fn8);
        List<Order> orders1 = new ArrayList<>();
        List<Order> orders2 = new ArrayList<>();
//        orders1.add(o1);
        orders1.add(o7); // Genova
        orders1.add(o6); // Venezia
        orders1.add(o8); // Athens
        orders1.add(o3); // Istanbul
        orders1.add(o2); // Odessa
        orders1.add(o5); // Nikolaev
        orders2.add(o4);
        Delivery d1 = new Delivery(0, "Sea delivery 0", city1, 1500, orders1);
        Delivery d2 = new Delivery(1, "Sea delivery 1", city1, 800, orders2);
        deliveries.add(d1);
        deliveries.add(d2);
        cities.add(city1);
        cities.add(city2);
        cities.add(city3);
        cities.add(city4);
        cities.add(city5);
        cities.add(city6);
        cities.add(city7);
        cities.add(city8);
        cities.add(city9);
        cities.add(city10);
        response = new BaseResponse(200);
    }

    @Override
    public Observable<List<Delivery>> getDeliveries() {
        return Observable.create(new Observable.OnSubscribe<List<Delivery>>() {
            @Override
            public void call(Subscriber<? super List<Delivery>> subscriber) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
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
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                subscriber.onNext(cities);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<Delivery>> addDelivery(@Body Delivery delivery) {
        return Observable.create(new Observable.OnSubscribe<List<Delivery>>() {
            @Override
            public void call(Subscriber<? super List<Delivery>> subscriber) {
//                try {
//                    //Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                deliveries.add(delivery);
                subscriber.onNext(deliveries);
                subscriber.onCompleted();
            }
        });
    }
}
