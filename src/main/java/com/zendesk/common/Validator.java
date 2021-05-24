package com.zendesk.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Validator {

    private static final Logger logger = LoggerFactory.getLogger(Validator.class);

    public static void validateDate (String date) throws RouteException {
        try {
            SimpleDateFormat sdfrmt = new SimpleDateFormat(Constants.DATE_FORMAT);
            sdfrmt.parse(date);
        } catch (ParseException ex) {
            logger.error(String.valueOf(ex));
            throw new RouteException(Errors.INVALID_DATE_FORMAT);
        }
    }

    public static void validateStations(String origin, String destination) throws RouteException {
        if (origin.equalsIgnoreCase(destination)) {
            throw new RouteException(Errors.SAME_ORIGIN_DESTINATION);
        }
    }
}
