/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.config.api;

import io.restassured.response.Response;
import org.sentinel.tests.utils.log.LoggerUtil;

import java.util.HashMap;
import java.util.Map;

import static org.sentinel.tests.constants.APIConstants.*;

/**
 * Manages API requests and handles header generation for different HTTP methods.
 * This class provides functionality to create and execute HTTP requests with appropriate headers.
 * 
 * <p>The class supports various HTTP methods including GET, POST, PUT, PATCH and file uploads.
 * It can generate headers with or without authentication tokens and handles content type specifications
 * based on the HTTP method being used.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 * APIRequestManager manager = new APIRequestManager("http://api.example.com");
 * Map<String, String> headers = manager.generateHeaders("POST", "auth-token");
 * Response response = manager.post("/endpoint", headers, requestBody);
 * </pre>
 *
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 * @see APIExecutor
 */
public class APIRequestManager {


    public APIRequestManager(String baseURI) {
        new APIExecutor(baseURI);
    }

    /**
     * Generates headers for API requests based on the HTTP method and token.
     * This method creates a map of headers to be used in the API request.
     *
     * @param httpMethod The HTTP method (e.g., GET, POST, PUT, DELETE)
     * @param token      The authentication token (optional)
     * @return A map of headers to be used in the API request
     */
    public Map<String, String> generateHeaders(String httpMethod, String token) {
        Map<String, String> headers = new HashMap<>();
        LoggerUtil.info(String.format("Request Header Creating for %s", httpMethod));
        headers.put(AUTHORIZATION, BEARER + token);
        LoggerUtil.info("Request header added: " + AUTHORIZATION + " = " + BEARER + token);
        headers.put(ACCEPT, ACCEPT_ALL);
        LoggerUtil.info("Request header added: " + ACCEPT + " = " + ACCEPT_ALL);
        switch (httpMethod.toUpperCase()) {
            case POST:
            case PUT:
            case PATCH:
                headers.put(CONTENT_TYPE, APPLICATION_JSON);
                LoggerUtil.info("Request header added: " + CONTENT_TYPE + " = " + APPLICATION_JSON);
                break;
            case UPLOAD:
                headers.put(CONTENT_TYPE, MULTIPART_FORMDATA);
                LoggerUtil.info("Request header added: " + CONTENT_TYPE + " = " + MULTIPART_FORMDATA);
                break;
            default:
                // No headers added for GET & DELETE
                LoggerUtil.info(String.format("No additional headers added for HTTP method : %s", httpMethod));
                break;
        }
        return headers;
    }

    /**
     * Generates headers for API requests based on the HTTP method.
     * This method creates a map of headers to be used in the API request.
     *
     * @param httpMethod The HTTP method (e.g., GET, POST, PUT, DELETE)
     * @return A map of headers to be used in the API request
     */
    public Map<String, String> generateHeaders(String httpMethod) {
        Map<String, String> headers = new HashMap<>();
        LoggerUtil.info(String.format("Request Header Creating for %s", httpMethod));
        switch (httpMethod.toLowerCase()) {
            case POST:
            case PUT:
            case PATCH:
                headers.put(CONTENT_TYPE, APPLICATION_JSON);
                LoggerUtil.info("Request header added: " + CONTENT_TYPE + " = " + APPLICATION_JSON);
                break;
            case UPLOAD:
                headers.put(CONTENT_TYPE, MULTIPART_FORMDATA);
                LoggerUtil.info("Request header added: " + CONTENT_TYPE + " = " + MULTIPART_FORMDATA);
                break;
            default:
                // No headers added for GET & DELETE
                LoggerUtil.info(String.format("No additional headers added for HTTP method : %s", httpMethod));
                break;
        }
        return headers;
    }

    
    /**
     * Sends a POST request to the specified path with the provided headers and body.
     *
     * @param path    the URL path to send the request to
     * @param headers a Map containing the HTTP headers to be included in the request
     * @param body    the request body as a String
     * @return a Response object containing the server's response
     */
    public Response post(String path, Map<String, String> headers, String body) {
        return APIExecutor.post(path, headers, body);
    }

    /**
     * Executes a GET request with the specified path.
     *
     * @param path The endpoint path for the GET request
     * @return The response object from the GET request
     */
    public Response get(String path) {
        return APIExecutor.get(path);
    }
}
