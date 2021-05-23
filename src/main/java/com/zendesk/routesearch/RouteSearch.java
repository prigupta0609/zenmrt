package com.zendesk.routesearch;

import com.zendesk.common.Errors;
import com.zendesk.common.RouteException;
import com.zendesk.model.Node;
import com.zendesk.model.Route;
import com.zendesk.model.Station;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RouteSearch {

    private static final Logger logger = LoggerFactory.getLogger(RouteSearch.class);

    @Autowired
    private RouteGraph routeGraph;

    /**
     * Get the shortest route from given origin and destination at certain date.
     * Following are the steps to identify route:
     * 1. Fetch the graph with given date.
     * 2. Identify shortest route using Dijkstra algorithm
     * @param origin
     * @param destination
     * @param date
     * @return
     * @throws RouteException
     */
    public Route getRoute(String origin, String destination, LocalDateTime date) throws RouteException {
        Map<Station, Set<Node>> graph = routeGraph.getGraphWithExistingStations(date);
        Station originStation = getStationFromStationName(origin, graph);
        Station destinationStation = getStationFromStationName(destination, graph);
        if (originStation != null && destinationStation != null) {
            Route route = getShortestPath(graph, originStation, destinationStation);
            logger.info("Route found with " + route.getRoute().size() + " stations");
            return route;
        } else {
            throw new RouteException(Errors.STATION_NOT_FOUND);
        }
    }

    /**
     * Fetch the shortest path using Dijkstra algorithm: https://www.geeksforgeeks.org/shortest-path-unweighted-graph/
     * predecessor -> maintain the station of predecessor for visited stations
     * distance -> store the time taken to reach a station from another station
     * path -> store the path being followed
     * @param graph
     * @param originStation
     * @param destinationStation
     * @return
     * @throws RouteException
     */
    private Route getShortestPath(Map<Station, Set<Node>> graph,
                                  Station originStation,
                                  Station destinationStation) throws RouteException {
        Map<Station, Station> predecessor = new HashMap<>();
        Map<Station, Integer> distance = new HashMap<>();
        if (isSourceDestConnected(graph, originStation, destinationStation, predecessor, distance) == false) {
            throw new RouteException(Errors.ORIGIN_DESTINATION_NOT_CONNECTED);
        }
        LinkedList<Station> path = new LinkedList<>();
        Station crawl = destinationStation;
        path.add(crawl);
        while (predecessor.get(crawl) != null) {
            path.add(predecessor.get(crawl));
            crawl = predecessor.get(crawl);
        }
        return getRouteFromStationNames(path);
    }

    /**
     * Identify if source and destination connected.
     * Also trace the path being followed to reach destination from origin.
     * visited -> marks the station as visited true/false
     * @param graph
     * @param originStation
     * @param destinationStation
     * @param predecessor
     * @param distance
     * @return
     */
    private boolean isSourceDestConnected(Map<Station, Set<Node>> graph,
                                          Station originStation,
                                          Station destinationStation,
                                          Map<Station, Station> predecessor,
                                          Map<Station, Integer> distance) {
        Map<Station, Boolean> visited = new HashMap<>();
        LinkedList<Station> queue = new LinkedList<>();

        for (Station station : graph.keySet()) {
            visited.put(station, false);
            distance.put(station, Integer.MAX_VALUE);
            predecessor.put(station, null);
        }

        visited.put(originStation, true);
        distance.put(originStation, 0);
        queue.add(originStation);

        while (!queue.isEmpty()){
            Station currentStation = queue.remove();
            Set<Node> neighbors = graph.get(currentStation);
            for (Node neighbor : neighbors) {
                Station neighborStation = neighbor.getStation();
                System.out.println("neighborStation -> " + neighborStation.getName());
                if (visited.get(neighborStation) == false) {
                    visited.put(neighborStation, true);
                    if (neighborStation.getName() != currentStation.getName())
                        distance.put(neighborStation, distance.get(currentStation) + 1);
                    else
                        distance.put(neighborStation, distance.get(currentStation));
                    predecessor.put(neighborStation, currentStation);
                    queue.add(neighborStation);

                    // stopping condition (when we find our destination)
                    if (neighborStation.getName().equalsIgnoreCase(destinationStation.getName()))
                        return true;
                }
            }
        }
        return false;
    }

    // Reverse travel the station list will give the path from origin to destination
    private Route getRouteFromStationNames(List<Station> stationList) {
        List<Station> path = new ArrayList<>();
        for (int i=stationList.size()-1; i>=0; i--) {
            path.add(stationList.get(i));
        }
        Route route = new Route(path);
        return route;
    }

    private Station getStationFromStationName (String stationName, Map<Station, Set<Node>> graph) {
        Set<Station> keySet = graph.keySet();
        for (Station s : keySet) {
            if (s.getName().equalsIgnoreCase(stationName))
                return s;
        }
        return null;
    }
}
