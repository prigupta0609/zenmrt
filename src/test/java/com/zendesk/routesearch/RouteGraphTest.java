package com.zendesk.routesearch;

import com.zendesk.model.Node;
import com.zendesk.model.Station;
import com.zendesk.model.StationCode;
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
        Map<Station, Set<Node>> graph = routeGraph.getGraphWithExistingStations(new LocalDateTime(1998, 01, 01, 0, 0, 0));
        Map<String, List<String>> expectedGraph = new HashMap<>();
        expectedGraph.put("CC8", new ArrayList<>(Arrays.asList("CC7")));
        expectedGraph.put("CC1", new ArrayList<>(Arrays.asList("CC3")));
        expectedGraph.put("CC4", new ArrayList<>(Arrays.asList("CC5","CC3")));
        expectedGraph.put("CC5", new ArrayList<>(Arrays.asList("CC4","CC7")));
        expectedGraph.put("CC7", new ArrayList<>(Arrays.asList("CC5","CC8")));
        expectedGraph.put("CC3", new ArrayList<>(Arrays.asList("CC1","CC4")));
        assertGraph(graph, expectedGraph);
    }

    @Test
    public void testGraphWithIntermediateDate_IncludingJunction() {
        List<Station> stations = stubStationList();
        Mockito.when(repository.getStationList()).thenReturn(stations);
        Map<Station, Set<Node>> graph = routeGraph.getGraphWithExistingStations(new LocalDateTime(2000, 02, 01, 0, 0, 0));
        Map<String, List<String>> expectedGraph = new HashMap<>();
        expectedGraph.put("CC8", new ArrayList<>(Arrays.asList("CC7")));
        expectedGraph.put("NS1", new ArrayList<>(Arrays.asList("NS2")));
        expectedGraph.put("NS2", new ArrayList<>(Arrays.asList("NS1","NS5","CC3")));
        expectedGraph.put("CC3", new ArrayList<>(Arrays.asList("CC4","CC2","NS2")));
        expectedGraph.put("NS5", new ArrayList<>(Arrays.asList("NS2","NS6")));
        expectedGraph.put("NS6", new ArrayList<>(Arrays.asList("NS5")));
        expectedGraph.put("CC1", new ArrayList<>(Arrays.asList("CC2")));
        expectedGraph.put("CC2", new ArrayList<>(Arrays.asList("CC3","CC1")));
        expectedGraph.put("CC4", new ArrayList<>(Arrays.asList("CC3","CC5")));
        expectedGraph.put("CC5", new ArrayList<>(Arrays.asList("CC4","CC7")));
        expectedGraph.put("CC7", new ArrayList<>(Arrays.asList("CC5","CC8")));
        assertGraph(graph, expectedGraph);
    }

    @Test
    public void testGraphBeforeMRTSystemStarted() {
        List<Station> stations = stubStationList();
        Mockito.when(repository.getStationList()).thenReturn(stations);
        Map<Station, Set<Node>> graph = routeGraph.getGraphWithExistingStations(new LocalDateTime(1980, 02, 01, 0, 0, 0));
        Assert.assertTrue(graph.isEmpty());
    }

    @Test
    public void testGraphAfterWholeEstablishment() {
        List<Station> stations = stubStationList();
        Mockito.when(repository.getStationList()).thenReturn(stations);
        Map<Station, Set<Node>> graph = routeGraph.getGraphWithExistingStations(new LocalDateTime(2030, 02, 01, 0, 0, 0));
        Map<String, List<String>> expectedGraph = new HashMap<>();
        expectedGraph.put("NS1", new ArrayList<>(Arrays.asList("NS2")));
        expectedGraph.put("NS2", new ArrayList<>(Arrays.asList("NS1","NS3","CC3")));
        expectedGraph.put("NS3", new ArrayList<>(Arrays.asList("NS2","NS4")));
        expectedGraph.put("NS4", new ArrayList<>(Arrays.asList("NS3","NS5","EW3")));
        expectedGraph.put("NS5", new ArrayList<>(Arrays.asList("NS4","NS6")));
        expectedGraph.put("NS6", new ArrayList<>(Arrays.asList("NS5")));
        expectedGraph.put("CC1", new ArrayList<>(Arrays.asList("CC2")));
        expectedGraph.put("CC2", new ArrayList<>(Arrays.asList("CC3","CC1")));
        expectedGraph.put("CC3", new ArrayList<>(Arrays.asList("CC4","CC2","NS2")));
        expectedGraph.put("CC4", new ArrayList<>(Arrays.asList("CC3","CC5")));
        expectedGraph.put("CC5", new ArrayList<>(Arrays.asList("CC4","CC6")));
        expectedGraph.put("CC6", new ArrayList<>(Arrays.asList("CC5","CC7","EW5")));
        expectedGraph.put("CC7", new ArrayList<>(Arrays.asList("CC6","CC8")));
        expectedGraph.put("CC8", new ArrayList<>(Arrays.asList("CC7")));
        expectedGraph.put("EW1", new ArrayList<>(Arrays.asList("EW2")));
        expectedGraph.put("EW2", new ArrayList<>(Arrays.asList("EW3","EW1")));
        expectedGraph.put("EW3", new ArrayList<>(Arrays.asList("EW2","EW4","NS4")));
        expectedGraph.put("EW4", new ArrayList<>(Arrays.asList("EW3","EW5")));
        expectedGraph.put("EW5", new ArrayList<>(Arrays.asList("EW4","CC6")));
        assertGraph(graph, expectedGraph);
    }

    private void assertGraph(Map<Station, Set<Node>> graph, Map<String, List<String>> expectedGraph) {
        Assert.assertEquals("Graph size don't match", expectedGraph.size(), graph.size());
        Assert.assertEquals("Station Codes don't match", expectedGraph.keySet(), getStationCodes(graph));
        for (String stationCode : expectedGraph.keySet()) {
            Assert.assertTrue("Neighboring stations don't match for " + stationCode, expectedGraph.get(stationCode)
                    .containsAll(getNeighborStationCodes(graph, stationCode)));
        }
    }

    private Set<String> getStationCodes(Map<Station, Set<Node>> graph) {
        Set<String> stations = new HashSet<>();
        for (Station station : graph.keySet()) {
            stations.add(station.getCode().getCode());
        }
        return stations;
    }

    private List<String> getNeighborStationCodes(Map<Station, Set<Node>> graph, String stationCode) {
        List<String> stationCodes = new ArrayList<>();
        Station s = getStation(graph, stationCode);
        for (Node node : graph.get(s)) {
            stationCodes.add(node.getStation().getCode().getCode());
        }
        return stationCodes;
    }

    private Station getStation(Map<Station, Set<Node>> graph, String stationCode) {
        Set<Station> keySet = graph.keySet();
        for (Station s : keySet) {
            if (s.getCode().getCode().equalsIgnoreCase(stationCode))
                return s;
        }
        return null;
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
