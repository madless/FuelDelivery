package main;

import main.entities.Edge;
import main.entities.Graph;
import main.entities.Vertex;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by dmikhov on 24.10.2016.
 */
public class DijkstraXmlParser {

    Graph graph;

    public Graph getGraph(String path) throws ParserConfigurationException, IOException, SAXException {
        graph = new Graph();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(path));
        visit(doc.getFirstChild());
        graph.tie();
        return graph;
    }

    private void visit(Node node) {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node childNode = list.item(i); // текущий нод
            process(childNode); // обработка
            visit(childNode); // рекурсия
        }
    }

    private void process(Node node) {
        if (node instanceof Element){
            Element e = (Element) node;
            NamedNodeMap atrs = e.getAttributes();
            switch (e.getTagName()) {
                case "vertex": {
                    int id = Integer.valueOf(atrs.getNamedItem("id").getNodeValue());
                    float lat = Float.valueOf(atrs.getNamedItem("lat").getNodeValue());
                    float lon = Float.valueOf(atrs.getNamedItem("long").getNodeValue());
                    Vertex vertex = new Vertex(id, lat, lon);
                    graph.putVertex(vertex);
//                    System.out.println(vertex);
                    break;
                }
                case "edge": {
                    int id = Integer.valueOf(atrs.getNamedItem("id").getNodeValue());
                    int firstId = Integer.valueOf(atrs.getNamedItem("firstId").getNodeValue());
                    int secondId = Integer.valueOf(atrs.getNamedItem("secondId").getNodeValue());
                    Edge edge = new Edge(id, firstId, secondId);
                    graph.putEdge(edge);
//                    System.out.println(edge);
                    break;
                }
            }
        }
    }
}
