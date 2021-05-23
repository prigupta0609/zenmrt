package com.zendesk.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

public class GetRouteResponse {

    @JsonProperty("route")
    @Nullable
    private Route route;

    @JsonProperty("error")
    @Nullable
    private Error error;

    public GetRouteResponse() {}

    public GetRouteResponse(@Nullable Route routes, @Nullable Error error) {
        this.route = routes;
        this.error = error;
    }

    @JsonProperty("route")
    public Route getRoute() {
        return route;
    }

    @JsonProperty("error")
    @Nullable
    public Error getError() {
        return error;
    }
}