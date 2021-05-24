package com.zendesk.common;

public enum Errors {

    STATION_NOT_FOUND("err_001", "Origin or destination station do not exist"),
    INVALID_DATE_FORMAT("err_002", "Invalid date format"),
    ORIGIN_DESTINATION_NOT_CONNECTED("err_003", "Origin and destination stations are not connected"),
    UNABLE_TO_PARSE_FILE("err_004", "Parsing error in the input file"),
    ISSUE_FETCHING_RESULTS("err_005", "There is some issue fetching results. Please try again after sometime"),
    SAME_ORIGIN_DESTINATION("err_006","Origin and destination can't be same");

    private String code;
    private String message;

    Errors(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
