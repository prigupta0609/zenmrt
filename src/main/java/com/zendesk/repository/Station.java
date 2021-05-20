package com.zendesk.model;

import java.util.Date;
import java.util.List;

public class Station {

    private final String name;
    private final List<String> code;
    private final Date startDate;

    public Station(String name, List<String> code, Date startDate) {
        this.name = name;
        this.code = code;
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public List<String> getCode() {
        return code;
    }

    public Date getStartDate() {
        return startDate;
    }
}
