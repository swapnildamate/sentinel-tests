package org.sentinel.tests.config.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.sentinel.tests.utils.log.LoggerUtil;

import java.util.Map;

public class APIExecutor {
    private String baseURI;
    private static RequestSpecification requestSpecification;
    private static Response response;

    APIExecutor(String baseURI) {
        this.baseURI = baseURI;
        RestAssured.baseURI = baseURI;// Example API
        requestSpecification = RestAssured.given().log().all();
    }

    private static void logResponse(Response response) {
        LoggerUtil.info("================= RESPONSE START =================");
        LoggerUtil.info(String.format("Status Code: %s", (response.getStatusCode())));
        // Log headers in a readable format
        LoggerUtil.info("Headers:");
        response.getHeaders().forEach(header -> LoggerUtil.info(String.format("Header Name: %s Value: %s", header.getName(), header.getValue())));
        try {
            String prettyBody = response.getBody().asPrettyString();
            LoggerUtil.info(String.format("Response Body: %s", prettyBody.toString().replaceAll("\\s+", " ")));
        } catch (Exception e) {
            LoggerUtil.warning(String.format("Failed to pretty-print response body. Raw Body: %s", response.getBody().asString()));
        }
        LoggerUtil.info("================= RESPONSE END =================");
    }

    static Response post(String path, Map<String, String> headers, String body) {
        response = requestSpecification
                .headers(headers) // Apply headers properly
                .body(body)       // Set request body
                .when()
                .post(path);
        logResponse(response);
        return response;
    }

    static Response get(String path) {
        response = requestSpecification       // Set request body
                .when()
                .get(path);
        logResponse(response);
        return response;
    }

}