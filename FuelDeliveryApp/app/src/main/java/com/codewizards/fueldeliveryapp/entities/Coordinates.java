package com.codewizards.fueldeliveryapp.entities;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class Coordinates {
    private double latitude;
    private double longitude;

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLat() {
        return latitude;
    }

    public void setLat(double latitude) {
        this.latitude = latitude;
    }

    public double getLon() {
        return longitude;
    }

    public void setLon(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return latitude + ":" + longitude;
    }
}
