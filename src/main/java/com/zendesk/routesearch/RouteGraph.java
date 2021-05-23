package com.zendesk.routesearch;

import com.zendesk.model.Edge;
import com.zendesk.model.Node;
import com.zendesk.model.Station;
import com.zendesk.model.Vertex;
import com.zendesk.repository.RouteRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RouteGraph{

    @Autowired
    private RouteRepository routeRepository;

    public Map<String, Vertex> getGraphWithExistingStations(LocalDateTime requestedDate) {
        Map<String, Vertex> graph = createRouteGraph(routeRepository.getStationList(), requestedDate);
        return graph;
    }

    private Map<String, Vertex> createRouteGraph(List<Station> stationList, LocalDateTime requestedDate) {
        Map<String, List<Station>> stationsPerLine = getStationsPerLine(stationList, requestedDate);
        Set<String> stationLines = stationsPerLine.keySet();
        List<Edge> edgeList = new ArrayList<>();
        for (String line : stationLines) {
            edgeList.addAll(getEdgeList(stationsPerLine.get(line)));
        }
        Map<Station, Set<Node>> neighborMap = getNeighborMap(edgeList);
        return populateGraph(neighborMap);
    }

    private Map<String, Vertex> populateGraph(Map<Station, Set<Node>> neighborMap) {
        Map<String, Vertex> graph = new HashMap<>();
        Set<Station> stations = neighborMap.keySet();
        for (Station station : stations) {
            // If graph already has vertex for this station, then add the stationCode to existing list.
            // Also, add the neighboring station in the list of neighbors.
            if (graph.containsKey(station.getName())) {
                Vertex existingVertex = graph.get(station.getName());
                existingVertex.getStationCodes().add(station.getCode());
                existingVertex.getNeighbors().addAll(neighborMap.get(station));
            } else {
                Vertex vertex = new Vertex(station.getName(),
                        new ArrayList<>(Arrays.asList(station.getCode())),
                        neighborMap.get(station));
                graph.put(station.getName(), vertex);
            }
        }
        return graph;
    }

    private Map<Station, Set<Node>> getNeighborMap(List<Edge> edgeList) {
        Map<Station, Set<Node>> neighborMap = new HashMap<>();
        for (Edge edge : edgeList) {
            neighborMap.put(edge.getOriginStation(), new HashSet<>());
            neighborMap.put(edge.getDestinationStation(), new HashSet<>());
        }
        for (Edge edge : edgeList) {
            neighborMap.get(edge.getOriginStation()).add(new Node(edge.getWeight(), edge.getDestinationStation()));
            neighborMap.get(edge.getDestinationStation()).add(new Node(edge.getWeight(), edge.getOriginStation()));
        }
        return neighborMap;
    }

    private List<Edge> getEdgeList(List<Station> stationList) {
        List<Edge> edgeList = new ArrayList<>();
        Station firstStation = stationList.get(0);
        Station prev = firstStation;
        for (int i=1; i<stationList.size(); i++) {
            Station current = stationList.get(i);
            edgeList.add(addEdge(prev, current, 1));
            prev = current;
        }
        return edgeList;
    }

    private Edge addEdge(Station prev, Station current, int weight) {
        return new Edge(prev, current, weight);
    }

    /**
     * Create station list per line.
     * We have created this stationList by reading CSV sequentially line by line which
     * indicates the origin and destination point for each station.
     * As arraylist maintain the insertion order, we are utilizing that property to maintain
     * the sequence of stations here as well.
     * @param stationList
     * @return
     */
    private Map<String, List<Station>> getStationsPerLine (List<Station> stationList, LocalDateTime requestedDate) {
        Map<String, List<Station>> stationsPerLine = new HashMap<>();
        for (Station station : stationList) {
            String stationLine = station.getCode().getLine();
            if (station.getStartDate().compareTo(requestedDate) <= 0) {
                if (stationsPerLine.containsKey(stationLine)) {
                    stationsPerLine.get(stationLine).add(station);
                } else {
                    stationsPerLine.put(stationLine, new ArrayList<>(Arrays.asList(station)));
                }
            }
        }
        return stationsPerLine;
    }
}
