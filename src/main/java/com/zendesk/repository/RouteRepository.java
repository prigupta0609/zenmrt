package com.zendesk.repository;

import com.zendesk.model.Station;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RouteRepository {

    private final List<Station> stationList = new ArrayList<>();

    public RouteRepository() {}

    public List<Station> getStationList() {
        return stationList;
    }
}
