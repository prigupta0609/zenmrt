package com.zendesk.model;

public class Edge {

    private Station originStation;
    private Station destinationStation;
    private int weight;

    public Edge(Station originStation, Station destinationStation, int weight) {
        this.originStation = originStation;
        this.destinationStation = destinationStation;
        this.weight = weight;
    }

    public Station getOriginStation() {
        return originStation;
    }

    public Station getDestinationStation() {
        return destinationStation;
    }

    public int getWeight() {
        return weight;
    }
}
