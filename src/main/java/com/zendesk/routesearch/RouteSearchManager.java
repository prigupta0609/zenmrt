package com.zendesk.routesearch;

import com.zendesk.common.RouteException;
import com.zendesk.contract.GetRouteRequest;
import com.zendesk.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RouteSearchManager {

    @Autowired
    private RouteSearch routeSearch;

    public Route getRoute(GetRouteRequest request) throws RouteException {
        return routeSearch.getRoute(request.getOrigin(),
                request.getDestination(),
                request.getDate());
    }
}
