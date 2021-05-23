package com.zendesk.routesearch;

import com.zendesk.common.RouteException;
import com.zendesk.model.Station;
import com.zendesk.model.StationCode;
import com.zendesk.model.Vertex;
import com.zendesk.repository.RouteRepository;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class RouteSearchTest {

    @InjectMocks
    private RouteSearch routeSearch = new RouteSearch();

    @Mock
    private RouteGraph routeGraph;

    @Test
    public void testRouteGraph() throws RouteException {
        RouteGraph routeGraph = new RouteGraph();
        Map<String, Vertex> graph = routeGraph.getGraphWithExistingStations(new LocalDateTime(2010, 01, 01, 0, 0, 0));
        routeSearch.getRoute("E", "P", new LocalDateTime(2010, 01, 01, 0, 0, 0));
    }

    private List<Station> stubStationList() {
        List<Station> stations = new ArrayList<>(Arrays.asList(
            new Station("A", new StationCode("NS1"), new LocalDateTime(1998, 01, 28, 0, 0, 0)),
            new Station("B", new StationCode("NS2"), new LocalDateTime(1998, 01, 28, 0, 0, 0)),
            new Station("C", new StationCode("NS3"), new LocalDateTime(2022, 01, 28, 0, 0, 0)),
            new Station("D", new StationCode("NS4"), new LocalDateTime(2002, 01, 28, 0, 0, 0)),
            new Station("E", new StationCode("NS5"), new LocalDateTime(1998, 01, 28, 0, 0, 0)),
            new Station("F", new StationCode("NS6"), new LocalDateTime(1998, 01, 28, 0, 0, 0)),
            new Station("G", new StationCode("CC1"), new LocalDateTime(1990, 01, 28, 0, 0, 0)),
            new Station("H", new StationCode("CC2"), new LocalDateTime(1999, 01, 28, 0, 0, 0)),
            new Station("B", new StationCode("CC3"), new LocalDateTime(1998, 01, 28, 0, 0, 0)),
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
