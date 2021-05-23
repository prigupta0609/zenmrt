package com.zendesk.model;

import java.util.List;

public class Route {

    private final List<Vertex> route;

    public Route(List<Vertex> route) {
        this.route = route;
    }

    public List<Vertex> getRoute() {
        return route;
    }
}
