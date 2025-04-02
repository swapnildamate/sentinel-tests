package org.sentinel.tests.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class JsonUtil {
        private static final String FILE_PATH = "D:\\git-hub\\SigmaTest\\automation-framework\\src\\data\\api\\T0001.json"; // JSON file path
    private static org.sentinel.tests.utils.log.Logger LoggerUtil;
    private static final Logger logger = LoggerUtil.getLogger(); // Logger instance

        // Load JSON from a file
        public static JSONArray loadJson() {
            JSONParser parser = new JSONParser();
            try (FileReader reader = new FileReader(FILE_PATH)) {
                JSONArray jsonArray = (JSONArray) parser.parse(reader);
                logger.info("JSON loaded successfully from " + FILE_PATH);
                return jsonArray;
            } catch (IOException | ParseException e) {
                logger.severe("Error loading JSON: " + e.getMessage());
            }
            return new JSONArray();
        }

        // Get a value by key from a specific JSON object (by ID)
        public static Object getValue(JSONArray jsonArray, long id, String key) {
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                if ((long) jsonObject.get("id") == id) {
                    logger.info("Retrieved value for ID " + id + ", Key: " + key);
                    return jsonObject.get(key);
                }
            }
            logger.warning("Key not found for ID " + id + ": " + key);
            return null;
        }

        // Update a key-value pair dynamically in a specific JSON object
        public static void updateValue(JSONArray jsonArray, long id, String key, Object newValue) {
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                if ((long) jsonObject.get("id") == id) {
                    jsonObject.put(key, newValue);
                    logger.info("Updated ID " + id + ", Key: " + key + " -> " + newValue);
                    return;
                }
            }
            logger.warning("Failed to update: ID " + id + " not found.");
        }

        // Write updated JSON back to the file
        public static void saveJson(JSONArray jsonArray) {
            try (FileWriter file = new FileWriter(FILE_PATH)) {
                file.write(jsonArray.toJSONString());
                file.flush();
                logger.info("JSON file updated successfully!");
            } catch (IOException e) {
                logger.severe("Error saving JSON: " + e.getMessage());
            }
        }
}
