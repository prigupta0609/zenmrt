package com.zendesk.builder;

import com.zendesk.common.RouteException;
import com.zendesk.common.Validator;
import com.zendesk.contract.GetRouteRequest;
import org.joda.time.LocalDateTime;

public class GetRouteRequestBuilder {

    /**
     * Build the request based on params and throw exception if request params are invalid
     * @param originName
     * @param destinationName
     * @param dateString
     * @return
     * @throws RouteException
     */
    public static GetRouteRequest getRouteRequest(String originName,
                                           String destinationName,
                                           String dateString) throws RouteException {
        GetRouteRequest request = new GetRouteRequest(originName, destinationName);
        LocalDateTime date;
        if (dateString == null) {
            date = new LocalDateTime();
        } else {
            Validator.validateDate(dateString);
            date = new LocalDateTime(dateString);
        }
        request.setDate(date);
        return request;
    }
}
