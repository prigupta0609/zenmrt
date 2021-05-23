package com.zendesk.routesearch;

import com.zendesk.common.Errors;
import com.zendesk.common.RouteException;
import com.zendesk.model.Node;
import com.zendesk.model.Route;
import com.zendesk.model.Vertex;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RouteSearch {

    @Autowired
    private RouteGraph routeGraph;

    public Route getRoute(String origin, String destination, LocalDateTime date) throws RouteException {
        Map<String, Vertex> graph = routeGraph.getGraphWithExistingStations(date);
        Vertex originVertex = graph.get(origin);
        Vertex destinationVertex = graph.get(destination);
        if (originVertex != null && destinationVertex != null)
            return getShortestPath(graph, originVertex, destinationVertex, date);
        throw new RouteException(Errors.STATION_NOT_FOUND);
    }

    private Route getShortestPath(Map<String, Vertex> graph,
                                   Vertex originVertex,
                                   Vertex destinationVertex,
                                   LocalDateTime date) throws RouteException {
        Map<String, String> predecessor = new HashMap<>();
        Map<String, Integer> distance = new HashMap<>();
        if (isSourceDestConnected(graph, originVertex, destinationVertex, date, predecessor, distance) == false) {
            throw new RouteException(Errors.ORIGIN_DESTINATION_NOT_CONNECTED);
        }
        LinkedList<String> path = new LinkedList<>();
        String crawl = destinationVertex.getStationName();
        path.add(crawl);
        while (predecessor.get(crawl) != null) {
            path.add(predecessor.get(crawl));
            crawl = predecessor.get(crawl);
        }
        return getRouteFromStationNames(path, graph);
    }

    private boolean isSourceDestConnected(Map<String, Vertex> graph,
                                          Vertex originVertex,
                                          Vertex destinationVertex,
                                          LocalDateTime date,
                                          Map<String, String> predecessor,
                                          Map<String, Integer> distance) {
        Map<String, Boolean> visited = new HashMap<>();
        LinkedList<String> queue = new LinkedList<>();

        for (String station : graph.keySet()) {
            visited.put(station, false);
            distance.put(station, Integer.MAX_VALUE);
            predecessor.put(station, null);
        }

        visited.put(originVertex.getStationName(), true);
        distance.put(originVertex.getStationName(), 0);
        queue.add(originVertex.getStationName());

        while (!queue.isEmpty()){
            String currentStation = queue.remove();
            Set<Node> neighbors = graph.get(currentStation).getNeighbors();
            for (Node neighbor : neighbors) {
                String neighborStation = neighbor.getStation().getName();
                if (visited.get(neighborStation) == false) {
                    visited.put(neighborStation, true);
                    distance.put(neighborStation, distance.get(currentStation) + 1);
                    predecessor.put(neighborStation, currentStation);
                    queue.add(neighborStation);

                    // stopping condition (when we find our destination)
                    if (neighborStation == destinationVertex.getStationName())
                        return true;
                }
            }
        }
        return false;
    }

    // Reverse travel the station list will give the path from origin to destination
    private Route getRouteFromStationNames(List<String> stationList, Map<String, Vertex> graph) {
        List<Vertex> path = new ArrayList<>();
        for (int i=stationList.size()-1; i>=0; i--) {
            path.add(graph.get(stationList.get(i)));
        }
        Route route = new Route(path);
        return route;
    }
}
