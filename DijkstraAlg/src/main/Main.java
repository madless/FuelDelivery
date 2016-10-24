package main;

import main.entities.Coordinates;
import main.entities.Graph;
import main.entities.Vertex;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Created by dmikhov on 24.10.2016.
 */
public class Main {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        String path = "data.xml";
        DijkstraXmlParser parser = new DijkstraXmlParser();
        Graph graph = parser.getGraph(path);
        Coordinates startCity = new Coordinates(46.975033, 31.994583); // Nikolaev
        Coordinates finalCity = new Coordinates(53.351989, -6.263181); // Dublin
//        Coordinates finalCity = new Coordinates(41.008238f, 28.978359f); // Istanbul
        DijkstraCalc dijkstraCalc = new DijkstraCalc();
        List<Vertex> shortestPath = dijkstraCalc.getShortestPath(graph, startCity, finalCity);
        System.out.println("SHORTEST PATH: " + shortestPath);
//        graph.print();
    }
}
