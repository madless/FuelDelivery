package com.codewizards.fueldeliveryapp.utils.dijkstra;


import com.codewizards.fueldeliveryapp.entities.Coordinates;
import com.codewizards.fueldeliveryapp.utils.Logger;
import com.codewizards.fueldeliveryapp.utils.dijkstra.entities.Vertex;

import java.util.List;

/**
 * Created by dmikhov on 24.10.2016.
 */
public class MapMath {
    private static Logger logger = Logger.getLogger(MapMath.class, false);
    static double PI_RAD = Math.PI / 180.0;
    final static double R = 6371.16;

    public static long distance(double lat1, double lon1, double lat2, double lon2){
        double dLon = Math.toRadians(lon2-lon1);
        double dLat = Math.toRadians(lat2-lat1);

        double a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)) +
                (Math.cos(Math.toRadians(lat1))) *
                        (Math.cos(Math.toRadians(lat2))) *
                        (Math.sin(dLon / 2)) *
                        (Math.sin(dLon / 2));
        double angle = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (long)(angle * R);
    }

    public static Vertex getNearest(List<Vertex> vertexes, Coordinates position) {
        long min = Long.MAX_VALUE;
        Vertex nearest = null;
        for (Vertex v: vertexes) {
            long dist = distance(v.getCoordinates().getLat(), v.getCoordinates().getLon(), position.getLat(), position.getLon());
            if(dist < min) {
                min = dist;
                nearest = v;
            }
        }
        logger.d("nearest: " + nearest + ", dist: " + min);
        return nearest;
    }
}
