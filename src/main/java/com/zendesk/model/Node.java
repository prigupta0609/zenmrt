package com.zendesk.model;

public class Node {

    private int weight;
    private Station station;

    public Node(int weight, Station station) {
        this.weight = weight;
        this.station = station;
    }

    public int getWeight() {
        return weight;
    }

    public Station getStation() {
        return station;
    }
}
