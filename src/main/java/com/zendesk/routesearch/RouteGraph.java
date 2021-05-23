package com.zendesk.routesearch;

import com.zendesk.model.Edge;
import com.zendesk.model.Node;
import com.zendesk.model.Station;
import com.zendesk.repository.RouteRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This component create graph based on give Date
 */
@Component
public class RouteGraph{

    @Autowired
    private RouteRepository routeRepository;

    /**
     * Fetch graph from the repository and create graph only for the
     * stations which exist on or before the given time.
     * @param requestedDate
     * @return
     */
    public Map<Station, Set<Node>> getGraphWithExistingStations(LocalDateTime requestedDate) {
        Map<Station, Set<Node>> graph = createRouteGraph(routeRepository.getStationList(), requestedDate);
        return graph;
    }

    /**
     * Following are the steps to create route graph:
     * 1. Fetch all stations per line.
     * 2. Join all the station on corresponding line with the edges.
     * 3. Join the stations which have junction i.e. stations with same name but different station code
     * 4. Find the neighbor for each station.
     *
     * NOTE: Stations which are at junction will be considered as neighbors.
     * @param stationList
     * @param requestedDate
     * @return
     */
    private Map<Station, Set<Node>> createRouteGraph(List<Station> stationList, LocalDateTime requestedDate) {
        Map<String, List<Station>> stationsPerLine = getStationsPerLine(stationList, requestedDate);
        Set<String> stationLines = stationsPerLine.keySet();
        List<Edge> edgeList = new ArrayList<>();
        for (String line : stationLines) {
            edgeList.addAll(getEdgeList(stationsPerLine.get(line)));
        }
        edgeList.addAll(getEdgesBetweenJunction(stationsPerLine));
        Map<Station, Set<Node>> neighborMap = getNeighborMap(edgeList);
        return neighborMap;
    }

    /**
     * Create neighbor map for each station.
     * @param edgeList
     * @return
     */
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

    /**
     * Attach neighboring stations in a given list with edges which will help in creating neighborMap.
     * As we are using list, the order in the list will identify the station order on a given line
     * @param stationList
     * @return
     */
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

    /**
     * Create edge between stations which are at junction.
     * For example:
     *      CC1,BuonaVista,20/03/2003
     *      NS1,BuonaVista,14/05/2010
     * As the above two station form a junction, we need to attach them via Edge
     * Following are the steps:
     * 1. Identify all the stations on our MRT map.
     * 2. Filter the stations which form junctions.
     * 3. Create an edge between all stations of each junction.
     * @param stationsPerLine
     * @return
     */
    private List<Edge> getEdgesBetweenJunction (Map<String, List<Station>> stationsPerLine) {
        List<Station> totalStationList = getTotalStations(stationsPerLine);
        Map<String, List<Station>> junctions = getJunctions(totalStationList);
        return joinJunction(junctions);
    }

    /**
     * Join the junction stations
     * @param junctions
     * @return
     */
    private List<Edge> joinJunction(Map<String, List<Station>> junctions) {
        List<Edge> edgeList = new ArrayList<>();
        List<String> commonStationList = junctions.keySet().stream()
                                        .filter(k -> junctions.get(k).size() > 1)
                                        .collect(Collectors.toList());
        for (String station : commonStationList) {
            List<Station> stationList = junctions.get(station);
            for (int i=0; i<stationList.size(); i++) {
                Station prev = stationList.get(i);
                for (int j=i+1; j<stationList.size(); j++) {
                    Station curr = stationList.get(j);
                    edgeList.add(addEdge(prev, curr, 1));
                }
            }
        }
        return edgeList;
    }

    /**
     * Identify junction stations
     * @param totalStationList
     * @return
     */
    private Map<String, List<Station>> getJunctions(List<Station> totalStationList) {
        Map<String, List<Station>> commonStations = new HashMap<>();
        for (Station station : totalStationList) {
            String name = station.getName();
            if (commonStations.get(name) != null) {
                List<Station> existingStations = commonStations.get(name);
                existingStations.add(station);
                commonStations.put(name, existingStations);
            } else {
                commonStations.put(name, new ArrayList<>(Arrays.asList(station)));
            }
        }
        return commonStations;
    }

    /**
     * Get all the stations present on all the MRT lines
     * @param stationsPerLine
     * @return
     */
    private List<Station> getTotalStations (Map<String, List<Station>> stationsPerLine) {
        List<Station> totalStationList = new ArrayList<>();
        for (String line : stationsPerLine.keySet()) {
            totalStationList.addAll(stationsPerLine.get(line));
        }
        return totalStationList;
    }
}
