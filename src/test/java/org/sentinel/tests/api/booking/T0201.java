package org.sentinel.tests.api.booking;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.sentinel.tests.base.BaseAPIService;
import org.sentinel.tests.utils.JsonUtil;
import org.sentinel.tests.utils.log.Logger;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class T0201 extends BaseAPIService {
    private final String dataFile = "booking.json";
    private Response response;

    @Test
    @Description("Verify user able to get access token.")
    public void getToken() throws NoSuchMethodException {

        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        Method method = this.getClass().getMethod(methodName);
        Description description = method.getAnnotation(Description.class);
        Logger.info(String.format("Test Case : %s Description :%s", methodName, description.value().toString()));

        String endpointPost = JsonUtil.getDataByIdAndKey(dataFile, "booking.getAuth", "post.CreateToken").toString();
        String reqBody = JsonUtil.getDataByIdAndKey(dataFile, "booking.getAuth", "reqBody").toString();

        //Step 1 Started
        Logger.info("Step 1: Started.....");
        response = bookingService.getToken(endpointPost, reqBody);
        assertLog.assertTrue(response.getStatusCode()==200,"Step 1 : Access token generated successfully.","Step 1 : User un-authorized.");

        assertLog.assertAllWithLog();
    }

    @Test
    @Description("Verify returns the ids of all the bookings that exist within the API..")
    public void getBookingsIds() throws NoSuchMethodException {

        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        Method method = this.getClass().getMethod(methodName);
        Description description = method.getAnnotation(Description.class);
        Logger.info(String.format("Test Case : %s Description :%s", methodName, description.value().toString()));

        String endpointGet = JsonUtil.getDataByIdAndKey(dataFile, "booking.geBookingIds", "get.BookingId").toString();

        //Step 1 Started
        Logger.info("Step 1: Started.....");
        response = bookingService.getBookingIds(endpointGet);
        assertLog.assertTrue(response.getStatusCode()==200,"Step 1 : Booking Ids displayed.","Step 1 : Booking Ids not displayed.");

        assertLog.assertAllWithLog();
    }
}
