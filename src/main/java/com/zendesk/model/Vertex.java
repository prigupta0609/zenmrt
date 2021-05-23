package com.zendesk.model;

import java.util.List;
import java.util.Set;

public class Vertex {

    private String stationName;
    private List<StationCode> stationCodes;
    private Set<Node> neighbors;

    public Vertex(String stationName, List<StationCode> stationCodes, Set<Node> neighbors) {
        this.stationName = stationName;
        this.stationCodes = stationCodes;
        this.neighbors = neighbors;
    }

    public String getStationName() {
        return stationName;
    }

    public List<StationCode> getStationCodes() {
        return stationCodes;
    }

    public Set<Node> getNeighbors() {
        return neighbors;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "stationName='" + stationName + '\'' +
                ", neighbors=" + neighbors +
                '}';
    }
}
