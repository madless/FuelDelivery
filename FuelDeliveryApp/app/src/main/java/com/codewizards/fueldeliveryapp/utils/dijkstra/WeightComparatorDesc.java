package com.codewizards.fueldeliveryapp.utils.dijkstra;


import com.codewizards.fueldeliveryapp.utils.dijkstra.entities.Edge;

import java.util.Comparator;

/**
 * Created by dmikhov on 24.10.2016.
 */
public class WeightComparatorDesc implements Comparator<Edge> {
    @Override
    public int compare(Edge e1, Edge e2) {
        return e1.getWeight() == e2.getWeight() ? 0 : e1.getWeight() > e2.getWeight() ? 1 : -1;
    }
}