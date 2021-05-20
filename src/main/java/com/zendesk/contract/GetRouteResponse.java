package com.zendesk.contract;

import javax.validation.constraints.NotNull;
import java.util.List;

public class GetRouteResponse {

    @NotNull
    private final List<Route> routes;

    public GetRouteResponse(List<Route> routes) {
        this.routes = routes;
    }

    public List<Route> getRoutes() {
        return routes;
    }
}
