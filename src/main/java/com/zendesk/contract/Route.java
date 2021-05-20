package com.zendesk.contract;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Route {

    @NotNull
    private final int priority;
    @NotNull
    private final List<Segment> segmentList;

    public Route(int priority, List<Segment> segmentList) {
        this.priority = priority;
        this.segmentList = segmentList;
    }

    public int getPriority() {
        return priority;
    }

    public List<Segment> getSegmentList() {
        return segmentList;
    }
}
