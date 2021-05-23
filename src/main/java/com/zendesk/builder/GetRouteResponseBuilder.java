package com.zendesk.builder;

import com.zendesk.common.RouteException;
import com.zendesk.contract.*;
import com.zendesk.contract.Error;
import com.zendesk.model.StationCode;
import com.zendesk.model.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GetRouteResponseBuilder {

    private static final Logger logger = LoggerFactory.getLogger(GetRouteResponseBuilder.class);

    public static String getRouteResponse(com.zendesk.model.Route path,
                                                    GetRouteRequest request) {
        logger.info("Total stations -> " + path.getRoute().size());
        Route route = getRoute(path.getRoute(), request);
        GetRouteResponse response = new GetRouteResponse(route, null);
        return GetRouteResponseBuilderHelper.stringifyResponse(response);
    }

    public static GetRouteResponse getErrorRouteResponse (RouteException ex) {
        Error error = new Error(ex.getCode(), ex.getMessage());
        GetRouteResponse response = new GetRouteResponse(null, error);
        return response;
    }

    private static Route getRoute(List<Vertex> path, GetRouteRequest request) {
        List<Segment> segments = new ArrayList<>();
        for (int i=0; i<path.size()-1; i++) {
            segments.add(getSegment(path.get(i), path.get(i+1)));
        }
        OriginDestination originDestination = new OriginDestination(request.getOrigin(), null, request.getDestination(), null);
        return new Route(segments, originDestination);
    }

    private static Segment getSegment(Vertex origin, Vertex destination) {
        String line = getCommonLine(origin.getStationCodes(), destination.getStationCodes()).get(0);
        OriginDestination originDestination = getOriginDestination(origin, destination);
        return new Segment(originDestination, line);
    }

    private static OriginDestination getOriginDestination(Vertex origin, Vertex destination) {
        return new OriginDestination(origin.getStationName(),
                getStationCode(origin),
                destination.getStationName(),
                getStationCode(destination));
    }

    private static String getStationCode (Vertex station) {
        StringBuilder builder = new StringBuilder("");
        List<StationCode> stationCodes = station.getStationCodes();
        for (int i=0; i<stationCodes.size(); i++) {
            builder.append(stationCodes.get(i).getLine()+stationCodes.get(i).getCode());
            if (i < stationCodes.size()-1) {
                builder.append("/");
            }
        }
        return builder.toString();
    }

    private static List<String> getCommonLine (List<StationCode> station1, List<StationCode> station2) {
        List<String> stationLines1 = getStationLines(station1);
        List<String> stationLines2 = getStationLines(station2);
        stationLines1.retainAll(stationLines2);
        return stationLines1;
    }

    private static List<String> getStationLines (List<StationCode> stationCodes) {
        List<String> lines = new ArrayList<>();
        for (StationCode code : stationCodes) {
            lines.add(code.getLine());
        }
        return lines;
    }
}
