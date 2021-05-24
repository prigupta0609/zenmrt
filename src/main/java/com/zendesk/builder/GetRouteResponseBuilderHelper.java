package com.zendesk.builder;

import com.zendesk.contract.GetRouteResponse;
import com.zendesk.contract.Route;
import com.zendesk.contract.Segment;

import java.util.List;

public class GetRouteResponseBuilderHelper {

    /**
     * Stringify the GetRouteResponse for display purpose
     * @param response
     * @return
     */
    public static String stringifyResponse (GetRouteResponse response) {
        if (response.getError() == null) {
            StringBuilder builder = new StringBuilder("");
            Route route = response.getRoute();
            buildOriginDestination(route, builder);
            buildTotalStations(route, builder);
            buildStationCodeList(route, builder);
            buildRouteList(route, builder);
            return builder.toString();
        } else {
            StringBuilder builder = new StringBuilder("Exception occurred : ");
            builder.append(response.getError().getCode())
                    .append(" -> ")
                    .append(response.getError().getMessage());
            return builder.toString();
        }
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
                .append("\n\n");
    }

    private static void buildRouteList(Route route, StringBuilder builder) {
        List<Segment> segments = route.getSegmentList();
        for (Segment segment : segments) {
            if (isLineChange(segment)) {
                builder.append("Change from ")
                        .append(getLineCode(segment.getOriginDestination().getOriginCode()))
                        .append(" line to ")
                        .append(getLineCode(segment.getOriginDestination().getDestinationCode()))
                        .append(" line\n");
            } else {
                builder.append("Take ")
                        .append(segment.getLineCode())
                        .append(" line from ")
                        .append(segment.getOriginDestination().getOriginName())
                        .append(" to ")
                        .append(segment.getOriginDestination().getDestinationName())
                        .append("\n");
            }
        }
    }

    private static String getLineCode (String stationCode) {
        return stationCode.substring(0,2);
    }

    private static boolean isLineChange (Segment segment) {
        String originName = segment.getOriginDestination().getOriginName();
        String destName = segment.getOriginDestination().getDestinationName();
        return originName.equalsIgnoreCase(destName);
    }
}
