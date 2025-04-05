/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.utils.insights;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.sentinel.tests.utils.log.LoggerUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for retrieving captured session recordings from LambdaTest API.
 * This class provides functionality to fetch video URLs and session data from
 * the LambdaTest automation API.
 *
 * <p>This class contains static utility methods and cannot be instantiated.</p>
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Retrieves video URLs for specific session IDs</li>
 *   <li>Handles API authentication using LambdaTest credentials</li>
 *   <li>Processes JSON responses from the LambdaTest API</li>
 * </ul>
 *
 * <p>Required environment variables:</p>
 * <ul>
 *   <li>LT_USER - LambdaTest username</li>
 *   <li>LT_ACCESS_KEY - LambdaTest access key</li>
 * </ul>
 *
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 */
public class GetCapturedSession {

    private GetCapturedSession() {
        // Private constructor to prevent instantiation
    }

    /**
     * Reads the response from the given HttpURLConnection and returns it as a String.
     *
     * @param connection The HttpURLConnection to read from
     * @return The response as a String
     * @throws IOException If an I/O error occurs while reading the response
     */
    private static String readResponse(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }

    /**
     * Retrieves the video URL for a given session ID from the LambdaTest API.
     *
     * @param sessionId The session ID to retrieve the video URL for
     * @return The video URL as a String, or null if not found
     * @throws IOException    If an I/O error occurs while fetching the video URL
     * @throws ParseException If an error occurs while parsing the JSON response
     */
    private static String getVideoUrl(String sessionId) throws IOException, ParseException {
        String apiUrl = String.format("https://api.lambdatest.com/automation/api/v1/sessions/%s/video?video_generated_status=true", sessionId);
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
        }

        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        String auth = String.format("%s:%s", System.getenv("LT_USER"), System.getenv("LT_ACCESS_KEY"));
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            LoggerUtil.error("Failed to fetch video URL. Response Code: " + responseCode);
            return null;
        }

        // Read response
        String response = readResponse(connection);
        connection.disconnect();

        // Parse JSON and extract video URL
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(response);

        if (jsonResponse.containsKey("url")) {
            return jsonResponse.get("url").toString();
        } else {
            LoggerUtil.error("Video URL not found in response.");
            return null;
        }
    }

    /**
     * Downloads the video from the given URL and saves it to a specified directory.
     *
     * @param videoUrl The URL of the video to download
     * @return The path to the downloaded video file
     * @throws IOException If an I/O error occurs while downloading the video
     */
    private static String downloadVideo(String videoUrl) throws IOException {
        // Generate dynamic filename with timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "Video_" + timestamp + ".mp4";

        // Define the save path: {user.dir}/reports/video/
        String saveDirectory = System.getProperty("user.dir") + "/reports/video/";
        Files.createDirectories(Paths.get(saveDirectory));  // Create dir if it doesn't exist

        // Full file path
        String filePath = saveDirectory + fileName;

        LoggerUtil.info("Saving video to: " + filePath);

        if (videoUrl == null || videoUrl.isEmpty()) {
            LoggerUtil.error("Video URL is null or empty. Cannot download.");
            return "Video URL is null or empty. Cannot download.";
        }

        HttpURLConnection connection = (HttpURLConnection) new URL(videoUrl).openConnection();
        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        // Read and save the video file
        try (InputStream inputStream = new BufferedInputStream(connection.getInputStream()); FileOutputStream fileOutputStream = new FileOutputStream(filePath); BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 8192)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, bytesRead);
            }
        }
        connection.disconnect();
        LoggerUtil.info("Video downloaded successfully." + filePath);
        return filePath;
    }

    /**
     * Retrieves the video path for a given session ID by downloading the video.
     *
     * @param sessionId The session ID to retrieve the video path for
     * @return The path to the downloaded video file
     */
    public static String getVideoPath(String sessionId) {
        try {
            return downloadVideo(getVideoUrl(sessionId));
        } catch (IOException | ParseException e) {
            throw new IllegalStateException("Unable to download video");
        }
    }
}