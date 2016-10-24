package com.codewizards.fueldeliveryapp.utils.dijkstra.entities;

import java.util.List;

/**
 * Created by madless on 24.10.2016.
 */
public class Path {
    List<Vertex> vertices;

    public Path(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    @Override
    public String toString() {
        return "Path{" +
                "vertices=" + vertices +
                '}';
    }
}
