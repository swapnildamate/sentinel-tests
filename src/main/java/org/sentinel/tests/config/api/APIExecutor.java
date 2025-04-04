/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.config.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.sentinel.tests.utils.log.LoggerUtil;

import java.util.Map;

/**
 * A utility class for executing HTTP API requests using REST Assured.
 * This class provides methods to make HTTP requests and handles response logging.
 * 
 * <p>The class maintains a static RequestSpecification and Response objects
 * to handle HTTP requests and responses. All responses are automatically logged
 * using the LoggerUtil.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 * APIExecutor executor = new APIExecutor("https://api.example.com");
 * Map<String, String> headers = new HashMap<>();
 * headers.put("Content-Type", "application/json");
 * Response response = APIExecutor.post("/endpoint", headers, jsonBody);
 * </pre>
 *
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 * @see io.restassured.RestAssured
 * @see io.restassured.response.Response
 * @see io.restassured.specification.RequestSpecification
 */
public class APIExecutor {
    private static RequestSpecification requestSpecification;
    private static Response response;

    /**
     * Constructor to initialize the APIExecutor with a base URI.
     *
     * @param baseURI The base URI for the API requests
     */
    APIExecutor(String baseURI) {
        RestAssured.baseURI = baseURI;// Example API
        requestSpecification = RestAssured.given().log().all();
    }

    /**
     * Logs the response details including status code, headers, and body.
     *
     * @param response The response object to log
     */
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

    /**
     * Executes a POST request with the specified path, headers, and body.
     *
     * @param path    The endpoint path for the POST request
     * @param headers The headers to include in the request
     * @param body    The body of the request
     * @return The response object from the POST request
     */
    static Response post(String path, Map<String, String> headers, String body) {
        response = requestSpecification
                .headers(headers) // Apply headers properly
                .body(body)       // Set request body
                .when()
                .post(path);
        logResponse(response);
        return response;
    }

    /**
     * Executes a GET request with the specified path.
     *
     * @param path The endpoint path for the GET request
     * @return The response object from the GET request
     */
    static Response get(String path) {
        response = requestSpecification       // Set request body
                .when()
                .get(path);
        logResponse(response);
        return response;
    }

}