package com.zendesk.common;

public class RouteException extends Exception {

    private String code;

    public RouteException (Errors error) {
        super(error.getMessage());
        this.code = error.getCode();
    }

    public String getCode() {
        return code;
    }
}
