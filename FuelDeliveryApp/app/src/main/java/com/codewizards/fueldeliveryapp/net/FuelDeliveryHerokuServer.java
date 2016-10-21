package com.codewizards.fueldeliveryapp.net;

import com.codewizards.fueldeliveryapp.utils.Const;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class FuelDeliveryHerokuServer {
    private static FuelDeliveryHerokuServer instance = new FuelDeliveryHerokuServer();
    private static FuelDeliveryApi api;
    private FuelDeliveryHerokuServer(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Const.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(FuelDeliveryApi.class);
    }

    public static FuelDeliveryApi getApi() {
        return api;
    }
}
