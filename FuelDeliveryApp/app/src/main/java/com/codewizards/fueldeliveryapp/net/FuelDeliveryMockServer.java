package com.codewizards.fueldeliveryapp.net;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class FuelDeliveryMockServer {
    private static FuelDeliveryMockImpl api;
    private FuelDeliveryMockServer(){
        api = new FuelDeliveryMockImpl();
    }
    public static FuelDeliveryApi getApi() {
        return api;
    }
}
