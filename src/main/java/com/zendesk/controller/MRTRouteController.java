package com.zendesk.controller;

import com.zendesk.builder.GetRouteRequestBuilder;
import com.zendesk.builder.GetRouteResponseBuilder;
import com.zendesk.common.RouteException;
import com.zendesk.contract.GetRouteRequest;
import com.zendesk.contract.GetRouteResponse;
import com.zendesk.model.Route;
import com.zendesk.model.Vertex;
import com.zendesk.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

// API mapping points
@RestController
public class MRTRouteController {

    @Autowired
    private RouteService routeService;

    @RequestMapping("/isActive")
    @ResponseBody
    public String isActive() throws Exception {
        return "MRT Router is active";
    }

    @GetMapping(value = "/route")
    @ResponseBody
    public String getRoutes(@RequestParam @NotNull String from,
                            @RequestParam @NotNull String to,
                            @RequestParam @Nullable String date) {
        try {
            GetRouteRequest request = GetRouteRequestBuilder.getRouteRequest(from, to, date);
            Route route = routeService.getRoute(request);
            return GetRouteResponseBuilder.getRouteResponse(route, request);
        } catch (RouteException e) {
            return null;
//            return GetRouteResponseBuilder.getErrorRouteResponse(e);
        }
    }
}
