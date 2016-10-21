package com.codewizards.fueldeliveryapp.net;

import com.codewizards.fueldeliveryapp.entities.City;
import com.codewizards.fueldeliveryapp.entities.Delivery;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by dmikhov on 21.10.2016.
 */
public interface FuelDeliveryApi {
    @GET("deliveries") Observable<List<Delivery>> getDeliveries();
    @GET("cities") Observable<List<City>> getCities();
}
