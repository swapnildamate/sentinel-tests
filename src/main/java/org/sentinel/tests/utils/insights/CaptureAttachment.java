/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.utils.insights;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility class for capturing test execution artifacts such as screenshots and videos.
 * This class provides static methods to capture and attach screenshots from WebDriver
 * and video recordings to test reports using Allure annotations.
 * 
 * The attachments are stored using Allure's @Attachment annotation which automatically
 * handles the attachment of binary data to test reports.
 *
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 * @see io.qameta.allure.Attachment
 * @see org.openqa.selenium.WebDriver
 * @see org.openqa.selenium.TakesScreenshot
 */
public class CaptureAttachment {

    private CaptureAttachment(){}

    /**
     * Captures a screenshot of the current browser window and creates an Allure attachment.
     * 
     * @param driver The WebDriver instance used to capture the screenshot. Can be null.
     * @return byte array containing the screenshot in PNG format, or null if driver is null
     * @see org.openqa.selenium.TakesScreenshot
     * @see io.qameta.allure.Attachment
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] captureScreenshot(WebDriver driver) {
        if (driver != null) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        return new byte[0];
    }

    /**
     * Captures video content from a specified path and attaches it to the test report.
     * This method reads a video file and converts it to a byte array for attachment purposes.
     * Uses @Attachment annotation to attach the video to Allure reports.
     *
     * @param videoPath The file system path to the video file to be captured
     * @return byte array containing the video content, or null if the file doesn't exist or cannot be read
     * @throws IOException If an I/O error occurs reading the video file
     */
    @Attachment(value = "Test Execution Video", type = "video/mp4")
    public static byte[] captureVideo(String videoPath) {
        try {
            if (videoPath != null && Files.exists(Paths.get(videoPath))) {
                return Files.readAllBytes(Paths.get(videoPath)); // Read video as byte[]
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}

