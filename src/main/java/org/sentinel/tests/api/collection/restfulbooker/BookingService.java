package org.sentinel.tests.api.collection.restfulbooker;

import io.restassured.response.Response;
import org.sentinel.tests.config.api.APIRequestManager;

import static org.sentinel.tests.constants.APIConstants.POST;

public class BookingService {
    APIRequestManager apiRequestManager;
    public BookingService(String baseURI){
       this.apiRequestManager=new APIRequestManager(baseURI);
    }

    public Response getToken(String path, String reqBody) {

        return apiRequestManager.post(path, apiRequestManager.generateHeaders(POST), reqBody);
    }

    public Response getBookingIds(String path) {
        return apiRequestManager.get(path);
    }

    public Response createBooking(String path, String reqBody) {
        return apiRequestManager.post(path, apiRequestManager.generateHeaders(POST), reqBody);
    }
}
