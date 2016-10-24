package main;

import main.entities.Coordinates;
import main.entities.Vertex;

import java.util.List;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by dmikhov on 24.10.2016.
 */
public class MapMath {
    static double PI_RAD = Math.PI / 180.0;
    final static double R = 6371.16;

//    public static long distance(double lat1, double long1, double lat2, double long2) {
//        double phi1 = lat1 * PI_RAD;
//        double phi2 = lat2 * PI_RAD;
//        double lam1 = long1 * PI_RAD;
//        double lam2 = long2 * PI_RAD;
//        double dist = 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1)); // km
//        return (long) dist;
//    }

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

//    public static long distance(double  lat1, double  lon1, double  lat2, double  lon2) {
////        double  pk = (double) (180.f/Math.PI);
////
////        double  a1 = lat_a / pk;
////        double  a2 = lng_a / pk;
////        double  b1 = lat_b / pk;
////        double  b2 = lng_b / pk;
////
////        double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
////        double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
////        double t3 = Math.sin(a1)*Math.sin(b1);
////        double tt = Math.acos(t1 + t2 + t3);
////
////        return (long) (6366000*tt);
//
//        double R = 6371; // km
//        double dLat = (lat2-lat1)*Math.PI/180;
//        double dLon = (lon2-lon1)*Math.PI/180;
//        lat1 = lat1*Math.PI/180;
//        lat2 = lat2*Math.PI/180;
//
//        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
//                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//        double d = R * c;
//        return (long) d;
//    }



//    public static int distance(double  lat1, double  lng1, double  lat2, double  lng2) {
//        double earthRadius = 6371000; // meters
//        double dLat = Math.toRadians(lat2-lat1);
//        double dLng = Math.toRadians(lng2-lng1);
//        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
//                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
//                        Math.sin(dLng/2) * Math.sin(dLng/2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//        double  dist = (double ) (earthRadius * c);
//        return (int) dist;
//    }

    public static Vertex getNearest(List<Vertex> vertexes, Coordinates position) {
        long min = Long.MAX_VALUE;
        Vertex nearest = null;
        for (Vertex v: vertexes) {
            long dist = distance(v.getCoordinates().getLat(), v.getCoordinates().getLon(), position.getLat(), position.getLon());
            System.out.println("v: " + v + ", dist: " + dist);
            if(dist < min) {
                min = dist;
                nearest = v;
                System.out.println("nearest: " + nearest + ", dist: " + dist) ;
            }
        }
        System.out.println("--------");
        return nearest;
    }
}
