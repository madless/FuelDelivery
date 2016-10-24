package main.entities;

import main.MapMath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmikhov on 24.10.2016.
 */
public class Graph {
    List<Vertex> vertexes = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();

    public Graph(List<Vertex> vertexes, List<Edge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public Graph() {
    }

    public List<Vertex> getVertexes() {
        return vertexes;
    }

    public void setVertexes(List<Vertex> vertexes) {
        this.vertexes = vertexes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public Vertex getVertexById(int id) {
        for (Vertex v: vertexes) {
            if(v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    public Edge getEdgeById(int id) {
        for (Edge e: edges) {
            if(e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    public int getNextVertexId() {
        int max = 0;
        for (Vertex v: vertexes) {
            if(v.getId() > max) {
                max = v.getId();
            }
        }
        return max + 1;
    }

    public int getNextEdgeId() {
        int max = 0;
        for (Edge e: edges) {
            if(e.getId() > max) {
                max = e.getId();
            }
        }
        return max + 1;
    }

    public void putVertex(Vertex vertex) {
        vertexes.add(vertex);
    }

    public void putEdge(Edge edge) {
        edges.add(edge);
    }

    public void tie() {
        for (Vertex v: vertexes) {
            v.getEdges().clear();
        }
        for (Edge e: edges) {
            Vertex first = getVertexById(e.getFirstId());
            Vertex second = getVertexById(e.getSecondId());
            long weight = MapMath.distance(first.getCoordinates().getLat(), first.getCoordinates().getLon(), second.getCoordinates().getLat(), second.getCoordinates().getLon());
            e.setFirst(first);
            e.setSecond(second);
            e.setWeight(weight);
            first.getEdges().add(e);
            second.getEdges().add(e);
        }
    }

    public void print() {
        System.out.println("Vertexes: ");
        for (Vertex v: vertexes) {
            System.out.println(v);
        }
        System.out.println("");
        System.out.println("Edges:");
        for (Edge e: edges) {
            System.out.println(e);
        }
    }
}
