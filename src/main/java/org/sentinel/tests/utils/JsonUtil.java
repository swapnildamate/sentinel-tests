/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.sentinel.tests.utils.log.LoggerUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility class for handling JSON operations including loading, retrieving, updating, and saving JSON data.
 * This class provides methods to manipulate JSON files and their contents through a simple interface.
 * 
 * The class supports:
 * - Loading JSON data from files
 * - Retrieving specific values by ID and key
 * - Updating values in JSON objects
 * - Saving modified JSON data back to files
 * 
 * All operations are logged using the LoggerUtil class for tracking and debugging purposes.
 * 
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 */
public class JsonUtil {
    static String FILE_PATH;

    // Load JSON from a file
    public static JSONArray loadJson(String fileName) {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + "src" + File.separator + "data-files" + File.separator + "jsons" + File.separator + fileName;
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(filePath)) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            LoggerUtil.info("JSON loaded successfully from " + filePath);
            return jsonArray;
        } catch (IOException | ParseException e) {
            LoggerUtil.error("Error loading JSON: " + e.getMessage());
        }
        return new JSONArray();
    }

    // Get a value by key from a specific JSON object (by ID)
    public static Object getValue(JSONArray jsonArray, long id, String key) {
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            if ((long) jsonObject.get("id") == id) {
                LoggerUtil.info("Retrieved value for ID " + id + ", Key: " + key);
                return jsonObject.get(key);
            }
        }
        LoggerUtil.warning("Key not found for ID " + id + ": " + key);
        return null;
    }

    // Update a key-value pair dynamically in a specific JSON object
    public static void updateValue(JSONArray jsonArray, long id, String key, Object newValue) {
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            if ((long) jsonObject.get("id") == id) {
                jsonObject.put(key, newValue);
                LoggerUtil.info("Updated ID " + id + ", Key: " + key + " -> " + newValue);
                return;
            }
        }
        LoggerUtil.warning("Failed to update: ID " + id + " not found.");
    }

    // Write updated JSON back to the file
    public static void saveJson(JSONArray jsonArray) {
        try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(jsonArray.toJSONString());
            file.flush();
            LoggerUtil.info("JSON file updated successfully!");
        } catch (IOException e) {
            LoggerUtil.error("Error saving JSON: " + e.getMessage());
        }
    }

    public static Object getDataByIdAndKey(String fileName, String id, String key) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(loadJson(fileName).toJSONString());
            for (Object obj : jsonArray) {
                JSONObject jsonObj = (JSONObject) obj;
                if (jsonObj.get("testCaseID").equals(id) && jsonObj.containsKey(key)) {
                    return jsonObj.get(key);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

//
//    public static void updateDynamicValues() {
//        updateDynamicValuesWithPattern("random", UUID.randomUUID().toString());
//    }
//
//    public static void updateDynamicValuesWithPattern(String pattern, String replacement) {
//        try {
//            JSONParser parser = new JSONParser();
//            JSONArray jsonArray = (JSONArray) parser.parse(JSON_STRING);
//
//            for (Object obj : jsonArray) {
//                JSONObject jsonObj = (JSONObject) obj;
//                for (Object key : jsonObj.keySet()) {
//                    String value = (String) jsonObj.get(key);
//                    if (value.equals("{{" + pattern + "}}")) {
//                        jsonObj.put(key, replacement);
//                    }
//                }
//            }
//
//            JSON_STRING = jsonArray.toJSONString();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
}
