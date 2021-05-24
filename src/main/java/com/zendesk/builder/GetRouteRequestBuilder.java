package com.zendesk.builder;

import com.zendesk.common.Errors;
import com.zendesk.common.RouteException;
import com.zendesk.common.Validator;
import com.zendesk.contract.GetRouteRequest;
import org.joda.time.LocalDateTime;

public class GetRouteRequestBuilder {

    /**
     * Build the request based on params and throw exception if request params are invalid
     * @throws RouteException
     */
    public static GetRouteRequest getRouteRequest(String originName,
                                           String destinationName,
                                           String dateString) throws RouteException {
        validateRequest(originName, destinationName);
        GetRouteRequest request = new GetRouteRequest(originName, destinationName);
        LocalDateTime date;
        if (dateString == null) {
            date = new LocalDateTime();
        } else {
            Validator.validateDate(dateString);
            date = getDate(dateString);
        }
        request.setDate(date);
        return request;
    }

    private static void validateRequest(String originName,
                                           String destinationName) throws RouteException {
        Validator.validateStations(originName, destinationName);
    }

    /**
     * Format date string to LocalDateTime
     */
    private static LocalDateTime getDate(String dateString) throws RouteException {
        try {
            String[] params = dateString.split("/");
            int day = Integer.parseInt(params[0]);
            int month = Integer.parseInt(params[1]);
            int year = Integer.parseInt(params[2]);
            return new LocalDateTime(year, month, day, 0, 0, 0);
        } catch (NumberFormatException e) {
            throw new RouteException(Errors.INVALID_DATE_FORMAT);
        }
    }
}
