/*
 * Copyright (c) 2025 sentinel-tests
 * All rights reserved.
 */
package org.sentinel.tests.utils;

import org.sentinel.tests.utils.log.LoggerUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;


/**
 * Utility class providing file and directory manipulation operations.
 * This class contains static methods for deleting files and directories.
 * All operations are performed safely with proper error handling and logging.
 *
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 */
public class FileUtil {

    /**
     * Main method for testing the deleteDir and deleteFile methods.
     *
     * @param directory Directory to delete.
     */
    public static void deleteDir(Path directory) {
        String userDir = System.getProperty("user.dir");
        try {
            Files.walk(Path.of(userDir + File.separator + directory))
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            LoggerUtil.info("Path deleted: " + path);
                        } catch (IOException e) {
                            LoggerUtil.warning("Unable to delete " + path + ": " + e.getMessage());
                        }
                    });
            LoggerUtil.info("Directory deleted successfully.");
        } catch (IOException e) {
            LoggerUtil.error("Error walking the file tree: " + e.getMessage());
        }
    }

    /**
     * Deletes a file at the specified path.
     * If the file does not exist, a warning is logged.
     * If an error occurs during deletion, an error message is logged.
     *
     * @param filePath The path of the file to be deleted
     */
    public static void deleteFile(Path filePath) {
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                LoggerUtil.info("File deleted successfully: " + filePath);
            } else {
                LoggerUtil.warning("File not found: " + filePath);
            }
        } catch (IOException e) {
            LoggerUtil.error("Unable to delete file " + filePath + ": " + e.getMessage());
        }
    }
}
