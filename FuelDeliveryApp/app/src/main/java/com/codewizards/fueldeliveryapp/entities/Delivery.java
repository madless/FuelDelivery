package com.codewizards.fueldeliveryapp.entities;

import java.util.List;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class Delivery {
    private int id;
    private String name;
    private City sourceCity;
    private FuzzyNumber amountOfFuel; // amount of fuel on a ship
    private List<Order> orders;

    public Delivery(int id, String name, City sourceCity, int amountOfFuel, List<Order> orders) {
        this.id = id;
        this.name = name;
        this.sourceCity = sourceCity;
        this.amountOfFuel = new FuzzyNumber(amountOfFuel, amountOfFuel, amountOfFuel);
        this.orders = orders;
    }

    public Delivery(int id, String name, int amountOfFuel, List<Order> orders) {
        this.id = id;
        this.orders = orders;
        this.name = name;
        this.amountOfFuel = new FuzzyNumber(amountOfFuel, amountOfFuel, amountOfFuel);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public FuzzyNumber getAmountOfFuel() {
        return amountOfFuel;
    }

    public void setAmountOfFuel(FuzzyNumber amountOfFuel) {
        this.amountOfFuel = amountOfFuel;
    }

    public City getSourceCity() {
        return sourceCity;
    }

    public void setSourceCity(City sourceCity) {
        this.sourceCity = sourceCity;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", orders=" + orders +
                '}';
    }
}
