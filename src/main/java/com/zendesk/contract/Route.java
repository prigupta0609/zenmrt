package com.zendesk.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Route {

    @JsonProperty("requestedOriginDestination")
    @Nullable
    private OriginDestination requestedOriginDestination;

    @JsonProperty("segmentList")
    @NotNull
    private List<Segment> segmentList;

    public Route() {}

    public Route(List<Segment> segmentList, OriginDestination requestedOriginDestination) {
        this.segmentList = segmentList;
        this.requestedOriginDestination = requestedOriginDestination;
    }

    @JsonProperty("segmentList")
    public List<Segment> getSegmentList() {
        return segmentList;
    }

    @JsonProperty("requestedOriginDestination")
    @Nullable
    public OriginDestination getRequestedOriginDestination() {
        return requestedOriginDestination;
    }
}
