package com.codewizards.fueldeliveryapp.utils.dijkstra.entities;

import com.codewizards.fueldeliveryapp.entities.City;

import java.util.List;

/**
 * Created by madless on 24.10.2016.
 */
public class Path {
    City from;
    City to;
    List<Vertex> vertices;

    public Path(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public Path(City from, City to, List<Vertex> vertices) {
        this.from = from;
        this.to = to;
        this.vertices = vertices;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public City getFrom() {
        return from;
    }

    public void setFrom(City from) {
        this.from = from;
    }

    public City getTo() {
        return to;
    }

    public void setTo(City to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Path{" +
                "vertices=" + vertices +
                '}';
    }
}
