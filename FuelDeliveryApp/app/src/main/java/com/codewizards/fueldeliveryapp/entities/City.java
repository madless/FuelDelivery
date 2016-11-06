package com.codewizards.fueldeliveryapp.entities;

/**
 * Created by dmikhov on 21.10.2016.
 */
public class City {
    private int id;
    private String name;
    private Coordinates coordinates;

    public City(int id, String name, Coordinates coordinates) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
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

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

//    @Override
//    public String toString() {
//        return "City{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", coordinates=" + coordinates +
//                '}';
//    }


    @Override
    public String toString() {
        return name;
    }
}
