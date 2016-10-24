package main.entities;

/**
 * Created by dmikhov on 24.10.2016.
 */
public class Edge {
    int id;
    int firstId;
    int secondId;
    Vertex first;
    Vertex second;
    long weight;

    public int getId() {
        return id;
    }

    public Edge(int id, int firstId, int secondId) {
        this.id = id;
        this.firstId = firstId;
        this.secondId = secondId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFirstId() {
        return firstId;
    }

    public void setFirstId(int firstId) {
        this.firstId = firstId;
    }

    public int getSecondId() {
        return secondId;
    }

    public void setSecondId(int secondId) {
        this.secondId = secondId;
    }

    public Vertex getFirst() {
        return first;
    }

    public void setFirst(Vertex first) {
        this.first = first;
    }

    public Vertex getSecond() {
        return second;
    }

    public void setSecond(Vertex second) {
        this.second = second;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "" + id;
//                ", first=" + first.getId() +
//                ", second=" + second.getId() +
//                ", weight=" + weight +
    }
}
