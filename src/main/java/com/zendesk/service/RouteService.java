package com.zendesk.service;

import com.zendesk.common.RouteException;
import com.zendesk.contract.GetRouteRequest;
import com.zendesk.model.Route;
import com.zendesk.routesearch.RouteSearchManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteService {

    @Autowired
    private RouteSearchManager manager;

    public Route getRoute(GetRouteRequest request) throws RouteException {
        return manager.getRoute(request);
    }
}
