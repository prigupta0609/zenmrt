package com.zendesk.builder;

import com.zendesk.contract.GetRouteResponse;
import com.zendesk.contract.Route;
import com.zendesk.contract.Segment;

import java.util.List;

public class GetRouteResponseBuilderHelper {

    public static String stringifyResponse (GetRouteResponse response) {
        StringBuilder builder = new StringBuilder("");
        Route route = response.getRoute();
        buildOriginDestination(route, builder);
        buildTotalStations(route, builder);
        buildStationCodeList(route, builder);
        return builder.toString();
    }

    private static void buildOriginDestination(Route route, StringBuilder builder) {
        String origin = route.getRequestedOriginDestination().getOriginName();
        String destination = route.getRequestedOriginDestination().getDestinationName();
        builder.append("Travel from ")
                .append(origin)
                .append(" to ")
                .append(destination)
                .append("\n");
    }

    private static void buildTotalStations(Route route, StringBuilder builder) {
        builder.append("Stations travelled : ")
                .append(route.getSegmentList().size())
                .append("\n");
    }

    private static void buildStationCodeList(Route route, StringBuilder builder) {
        builder.append("Route : (");
        List<Segment> segmentList = route.getSegmentList();
        for (Segment segment : segmentList) {
            builder.append("'")
                    .append(segment.getOriginDestination().getOriginCode())
                    .append("', ");
        }
        Segment lastSegment = segmentList.get(segmentList.size()-1);
        builder.append("'")
                .append(lastSegment.getOriginDestination().getDestinationCode())
                .append("')")
                .append("\n");
    }

//    private static void buildRouteList(Route route, StringBuilder builder) {
//
//    }
}
