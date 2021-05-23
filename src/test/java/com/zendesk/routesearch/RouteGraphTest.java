package com.zendesk.routesearch;

import com.zendesk.model.Node;
import com.zendesk.model.Station;
import com.zendesk.model.StationCode;
import com.zendesk.model.Vertex;
import com.zendesk.repository.RouteRepository;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class RouteGraphTest {

    @InjectMocks
    private RouteGraph routeGraph = new RouteGraph();

    @Mock
    private RouteRepository repository;

    @Test
    public void testGraphWithIntermediateDate() {
        List<Station> stations = stubStationList();
        Mockito.when(repository.getStationList()).thenReturn(stations);
        Map<String, Vertex> graph = routeGraph.getGraphWithExistingStations(new LocalDateTime(1998, 01, 01, 0, 0, 0));
        Map<String, List<String>> expectedGraph = new HashMap<>();
        expectedGraph.put("P", new ArrayList<>(Arrays.asList("O")));
        expectedGraph.put("G", new ArrayList<>(Arrays.asList("B")));
        expectedGraph.put("I", new ArrayList<>(Arrays.asList("J","B")));
        expectedGraph.put("J", new ArrayList<>(Arrays.asList("I","O")));
        expectedGraph.put("O", new ArrayList<>(Arrays.asList("J","P")));
        expectedGraph.put("B", new ArrayList<>(Arrays.asList("G","I")));
        assertGraph(graph, expectedGraph);
    }

    @Test
    public void testGraphWithIntermediateDate_IncludingJunction() {
        List<Station> stations = stubStationList();
        Mockito.when(repository.getStationList()).thenReturn(stations);
        Map<String, Vertex> graph = routeGraph.getGraphWithExistingStations(new LocalDateTime(2000, 02, 01, 0, 0, 0));
        Map<String, List<String>> expectedGraph = new HashMap<>();
        expectedGraph.put("P", new ArrayList<>(Arrays.asList("O")));
        expectedGraph.put("A", new ArrayList<>(Arrays.asList("B")));
        expectedGraph.put("B", new ArrayList<>(Arrays.asList("A","E","I","H")));
        expectedGraph.put("E", new ArrayList<>(Arrays.asList("B","F")));
        expectedGraph.put("F", new ArrayList<>(Arrays.asList("E")));
        expectedGraph.put("G", new ArrayList<>(Arrays.asList("H")));
        expectedGraph.put("H", new ArrayList<>(Arrays.asList("B","G")));
        expectedGraph.put("I", new ArrayList<>(Arrays.asList("B","J")));
        expectedGraph.put("J", new ArrayList<>(Arrays.asList("I","O")));
        expectedGraph.put("O", new ArrayList<>(Arrays.asList("J","P")));
        assertGraph(graph, expectedGraph);
    }

    @Test
    public void testGraphBeforeMRTSystemStarted() {
        List<Station> stations = stubStationList();
        Mockito.when(repository.getStationList()).thenReturn(stations);
        Map<String, Vertex> graph = routeGraph.getGraphWithExistingStations(new LocalDateTime(1980, 02, 01, 0, 0, 0));
        Assert.assertTrue(graph.isEmpty());
    }

    @Test
    public void testGraphAfterWholeEstablishment() {
        List<Station> stations = stubStationList();
        Mockito.when(repository.getStationList()).thenReturn(stations);
        Map<String, Vertex> graph = routeGraph.getGraphWithExistingStations(new LocalDateTime(2030, 02, 01, 0, 0, 0));
        Map<String, List<String>> expectedGraph = new HashMap<>();
        expectedGraph.put("A", new ArrayList<>(Arrays.asList("B")));
        expectedGraph.put("B", new ArrayList<>(Arrays.asList("A","C","I","H")));
        expectedGraph.put("C", new ArrayList<>(Arrays.asList("B","D")));
        expectedGraph.put("D", new ArrayList<>(Arrays.asList("C","E","L","M")));
        expectedGraph.put("E", new ArrayList<>(Arrays.asList("D","F")));
        expectedGraph.put("F", new ArrayList<>(Arrays.asList("E")));
        expectedGraph.put("G", new ArrayList<>(Arrays.asList("H")));
        expectedGraph.put("H", new ArrayList<>(Arrays.asList("B","G")));
        expectedGraph.put("I", new ArrayList<>(Arrays.asList("B","J")));
        expectedGraph.put("J", new ArrayList<>(Arrays.asList("I","N")));
        expectedGraph.put("N", new ArrayList<>(Arrays.asList("J","O","M")));
        expectedGraph.put("O", new ArrayList<>(Arrays.asList("N","P")));
        expectedGraph.put("P", new ArrayList<>(Arrays.asList("O")));
        expectedGraph.put("K", new ArrayList<>(Arrays.asList("L")));
        expectedGraph.put("L", new ArrayList<>(Arrays.asList("D","K")));
        expectedGraph.put("M", new ArrayList<>(Arrays.asList("D","N")));
        assertGraph(graph, expectedGraph);
    }

    private void assertGraph(Map<String, Vertex> graph, Map<String, List<String>> expectedGraph) {
        Assert.assertEquals(expectedGraph.size(), graph.size());
        Assert.assertEquals(expectedGraph.keySet(), graph.keySet());
        for (String station : expectedGraph.keySet()) {
            Assert.assertTrue(expectedGraph.get(station).containsAll(getNeighborStationNames(graph.get(station))));
        }
    }

    private List<String> getNeighborStationNames(Vertex station) {
        List<String> stationNames = new ArrayList<>();
        for (Node node : station.getNeighbors()) {
            stationNames.add(node.getStation().getName());
        }
        return stationNames;
    }

    private List<Station> stubStationList() {
        List<Station> stations = new ArrayList<>(Arrays.asList(
            new Station("A", new StationCode("NS1"), new LocalDateTime(1998, 01, 28, 0, 0, 0)),
            new Station("B", new StationCode("NS2"), new LocalDateTime(1998, 01, 28, 0, 0, 0)),
            new Station("C", new StationCode("NS3"), new LocalDateTime(2022, 01, 28, 0, 0, 0)),
            new Station("D", new StationCode("NS4"), new LocalDateTime(2002, 01, 28, 0, 0, 0)),
            new Station("E", new StationCode("NS5"), new LocalDateTime(1998, 01, 28, 0, 0, 0)),
            new Station("F", new StationCode("NS6"), new LocalDateTime(1998, 01, 28, 0, 0, 0)),
            new Station("G", new StationCode("CC1"), new LocalDateTime(1992, 01, 28, 0, 0, 0)),
            new Station("H", new StationCode("CC2"), new LocalDateTime(1998, 01, 28, 0, 0, 0)),
            new Station("B", new StationCode("CC3"), new LocalDateTime(1992, 01, 28, 0, 0, 0)),
            new Station("I", new StationCode("CC4"), new LocalDateTime(1992, 01, 28, 0, 0, 0)),
            new Station("J", new StationCode("CC5"), new LocalDateTime(1992, 01, 28, 0, 0, 0)),
            new Station("N", new StationCode("CC6"), new LocalDateTime(2018, 01, 28, 0, 0, 0)),
            new Station("O", new StationCode("CC7"), new LocalDateTime(1992, 01, 28, 0, 0, 0)),
            new Station("P", new StationCode("CC8"), new LocalDateTime(1992, 01, 28, 0, 0, 0)),
            new Station("K", new StationCode("EW1"), new LocalDateTime(2004, 01, 28, 0, 0, 0)),
            new Station("L", new StationCode("EW2"), new LocalDateTime(2004, 01, 28, 0, 0, 0)),
            new Station("D", new StationCode("EW3"), new LocalDateTime(2002, 01, 28, 0, 0, 0)),
            new Station("M", new StationCode("EW4"), new LocalDateTime(2004, 01, 28, 0, 0, 0)),
            new Station("N", new StationCode("EW5"), new LocalDateTime(2018, 01, 28, 0, 0, 0))
        ));
        return stations;
    }
}
