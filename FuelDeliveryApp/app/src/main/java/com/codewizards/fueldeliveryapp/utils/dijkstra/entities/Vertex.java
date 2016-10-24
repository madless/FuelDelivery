package com.codewizards.fueldeliveryapp.utils.dijkstra.entities;

import com.codewizards.fueldeliveryapp.entities.Coordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmikhov on 24.10.2016.
 */
public class Vertex {
    int id;
    long value = Long.MAX_VALUE;
    List<Vertex> minPath = new ArrayList<>();
    Coordinates coordinates;
    List<Integer> edgeIds = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();
    boolean isVisited;

    public Vertex(int id, double lat, double lon) {
        this.id = id;
        this.coordinates = new Coordinates(lat, lon);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public List<Vertex> getMinPath() {
        return minPath;
    }

    public void setMinPath(List<Vertex> minPath) {
        this.minPath = new ArrayList<>();
        this.minPath.addAll(minPath);
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public List<Integer> getEdgeIds() {
        return edgeIds;
    }

    public void setEdgeIds(List<Integer> edgeIds) {
        this.edgeIds = edgeIds;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        return id == vertex.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return id + ", c=" + coordinates;
//                ", value=" + value +
//                ", minPath=" + minPath;
//

    }
}
