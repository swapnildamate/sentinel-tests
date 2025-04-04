package org.sentinel.tests.config.api;

import io.restassured.response.Response;
import org.sentinel.tests.utils.log.LoggerUtil;

import java.util.HashMap;
import java.util.Map;

import static org.sentinel.tests.constants.APIConstants.*;

public class APIRequestManager {

    public APIRequestManager(String baseURI) {
        new APIExecutor(baseURI);
    }

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

    public Response post(String path, Map<String, String> headers, String body) {
        return APIExecutor.post(path, headers, body);
    }

    public Response get(String path) {
        return APIExecutor.get(path);
    }
}
