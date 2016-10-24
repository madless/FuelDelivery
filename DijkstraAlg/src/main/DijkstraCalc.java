package main;

import main.entities.Coordinates;
import main.entities.Edge;
import main.entities.Graph;
import main.entities.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dmikhov on 24.10.2016.
 */
public class DijkstraCalc {
    private Vertex start;
    private Vertex finish;
    public List<Vertex> getShortestPath(Graph graph, Coordinates startCity, Coordinates finalCity) {
        addSearchedCitiesToGraph(graph, startCity, finalCity);
        initStartValue();
        boolean isPassedAllGraph = false;
        while (!isPassedAllGraph) {
            Vertex minimal = findMinimal(graph);
            System.out.println("Minimal: " + minimal);
            if(minimal != null) {
                visit(minimal);
            } else {
                isPassedAllGraph = true;
            }
        }
        return finish.getMinPath();
    }

    public void addSearchedCitiesToGraph(Graph graph, Coordinates startCity, Coordinates finalCity) {
        int startIndex = graph.getNextVertexId();
        int finalIndex = startIndex + 1;
        start = new Vertex(startIndex, startCity.getLat(), startCity.getLon());
        finish = new Vertex(finalIndex, finalCity.getLat(), finalCity.getLon());
        Vertex startConnector = MapMath.getNearest(graph.getVertexes(), startCity);
        Vertex finishConnector = MapMath.getNearest(graph.getVertexes(), finalCity);
        System.out.println("startConnector: " + startConnector);
        System.out.println("finishConnector: " + finishConnector);
        int startEdgeIndex = graph.getNextEdgeId();
        int finalEdgeIndex = startEdgeIndex + 1;
        Edge startConnectorEdge = new Edge(startEdgeIndex, start.getId(), startConnector.getId());
        Edge finalConnectorEdge = new Edge(finalEdgeIndex, finish.getId(), finishConnector.getId());
        graph.putEdge(startConnectorEdge);
        graph.putEdge(finalConnectorEdge);
        graph.putVertex(start);
        graph.putVertex(finish);
        graph.tie();
    }

    private void initStartValue() {
        start.setValue(0);
        start.getMinPath().add(start);
    }

    public Vertex findMinimal(Graph graph) {
        long minValue = Long.MAX_VALUE;
        Vertex minimalVertex = null;
        for (Vertex v: graph.getVertexes()) {
            if(!v.isVisited() && v.getValue() < minValue) {
                minValue = v.getValue();
                minimalVertex = v;
            }
        }
        return minimalVertex;
    }

    public void visit(Vertex v) {
        v.setVisited(true);
        List<Edge> edges = v.getEdges();
        Collections.sort(edges, new WeightComparatorAsc());
        for (Edge e: edges) {
            if(e.getFirst().equals(v)) {
                recalculateValue(v, e.getSecond(), e);
            } else {
                recalculateValue(v, e.getFirst(), e);
            }
        }
    }

    public void recalculateValue(Vertex startVertex, Vertex vertexToRecalculate, Edge e) {
        long newValue = startVertex.getValue() + e.getWeight();
        if(newValue < vertexToRecalculate.getValue()) {
            vertexToRecalculate.setValue(newValue);
            vertexToRecalculate.setMinPath(startVertex.getMinPath());
            vertexToRecalculate.getMinPath().add(vertexToRecalculate);
            System.out.println("vertexToRecalculate: " + vertexToRecalculate.getId() + ", set value: " + newValue + ", setMinPath: " + vertexToRecalculate.getMinPath());
        }
    }

    public List<Vertex> findAllNeighbours(Vertex v) {
        List<Edge> edges = v.getEdges();
        List<Vertex> neighbours = new ArrayList<>();
        for (Edge e: edges) {
            if(e.getFirst().equals(v)) {
                neighbours.add(e.getSecond());
            } else {
                neighbours.add(e.getFirst());
            }
        }
        return neighbours;
    }

    private void reset() {
        start = null;
        finish = null;
    }

}
