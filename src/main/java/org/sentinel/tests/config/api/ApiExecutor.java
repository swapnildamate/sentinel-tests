package org.sentinel.tests.config.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class ApiExecutor {
    private String baseURI;
    private static RequestSpecification requestSpecification;

    ApiExecutor(String baseURI) {
        this.baseURI = baseURI;
        RestAssured.baseURI = baseURI;// Example API
        requestSpecification = RestAssured.given().log().all();
    }

    static Response post(String path, Map<String, String> headers, String body) {
        return requestSpecification
                .headers(headers) // Apply headers properly
                .body(body)       // Set request body
                .when()
                .post(path);
    }

    static Response get(String path){
        return requestSpecification       // Set request body
                .when()
                .get(path);
    }
}
