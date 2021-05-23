package com.zendesk.builder;

import com.zendesk.common.RouteException;
import com.zendesk.contract.*;
import com.zendesk.contract.Error;
import com.zendesk.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for GetRouteResponse
 */
public class GetRouteResponseBuilder {

    private static final Logger logger = LoggerFactory.getLogger(GetRouteResponseBuilder.class);

    public static String getRouteResponse(com.zendesk.model.Route path,
                                                    GetRouteRequest request) {
        logger.info("Total stations -> " + path.getRoute().size());
        Route route = getRoute(path.getRoute(), request);
        GetRouteResponse response = new GetRouteResponse(route, null);
        return GetRouteResponseBuilderHelper.stringifyResponse(response);
    }

    public static String getErrorRouteResponse (RouteException ex) {
        Error error = new Error(ex.getCode(), ex.getMessage());
        GetRouteResponse response = new GetRouteResponse(null, error);
        return GetRouteResponseBuilderHelper.stringifyResponse(response);
    }

    private static Route getRoute(List<Station> path, GetRouteRequest request) {
        List<Segment> segments = new ArrayList<>();
        for (int i=0; i<path.size()-1; i++) {
            segments.add(getSegment(path.get(i), path.get(i+1)));
        }
        OriginDestination originDestination = new OriginDestination(request.getOrigin(), null, request.getDestination(), null);
        return new Route(segments, originDestination);
    }

    private static Segment getSegment(Station origin, Station destination) {
        String line = getCommonLine(origin, destination);
        OriginDestination originDestination = getOriginDestination(origin, destination);
        return new Segment(originDestination, line);
    }

    private static OriginDestination getOriginDestination(Station origin, Station destination) {
        return new OriginDestination(origin.getName(),
                origin.getCode().getCode(),
                destination.getName(),
                destination.getCode().getCode());
    }

    private static String getCommonLine (Station station1, Station station2) {
        if (station1.getCode().getLine().equalsIgnoreCase(station2.getCode().getLine())) {
            return station1.getCode().getLine();
        } else {
            return station2.getCode().getLine();
        }
    }
}
