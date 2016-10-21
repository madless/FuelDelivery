package com.codewizards.fueldeliveryapp.entities;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class Order {
    private int id;
    private City city;
    private FuzzyNumber amountOfFuel;

    public Order(int id, City city, FuzzyNumber amountOfFuel) {
        this.id = id;
        this.city = city;
        this.amountOfFuel = amountOfFuel;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", city=" + city +
                ", amountOfFuel=" + amountOfFuel +
                '}';
    }
}
