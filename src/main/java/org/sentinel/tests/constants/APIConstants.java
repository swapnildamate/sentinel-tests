/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.constants;

/**
 * Constants class containing HTTP-related string values used across the API.
 * This class provides commonly used HTTP methods, header names, and content types
 * as static final String constants.
 * 
 * <p>The constants are grouped into two main categories:
 * <ul>
 *   <li>HTTP Methods - Standard HTTP verbs and custom methods</li>
 *   <li>Headers - Common HTTP header names and their values</li>
 * </ul>
 * 
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 */
public class APIConstants {

    private APIConstants(){

    }

    //HTTP Methods
    public static final String GET="get";
    public static final String POST="post";
    public static final String PATCH="patch";
    public static final String PUT="put";
    public static final String DELETE="delete";
    public static final String UPLOAD="upload";

    //Headers
    public static final String ACCEPT="Accept";
    public static final String ACCEPT_ALL="*/*";
    public static final String CONTENT_TYPE="Content-Type";
    public static final String APPLICATION_VND_API_JSON="application/vnd.api+json";
    public static final String APPLICATION_JSON="application/json";
    public static final String AUTHORIZATION="Authorization";
    public static final String BEARER =  "Bearer ";
    public static final String TEXT_PLAIN="text/plain";
    public static final String MULTIPART_FORMDATA="multipart/form-data";

    //
    public static final String REQUEST_HEADER_ADDED="Request header added: ";
}
