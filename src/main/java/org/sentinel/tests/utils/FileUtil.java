package org.sentinel.tests.utils;

import org.sentinel.tests.utils.log.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class FileUtil {

    public static void deleteDir(Path directory) {
        String userDir = System.getProperty("user.dir");
        try {
            Files.walk(Path.of(userDir + File.separator + directory))
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            Logger.warning("Unable to delete " + path + ": " + e.getMessage());
                        }
                    });
            Logger.info("Directory deleted successfully.");
        } catch (IOException e) {
            Logger.error("Error walking the file tree: " + e.getMessage());
        }
    }

    // Method to delete a single file
    public static void deleteFile(Path filePath) {
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                Logger.info("File deleted successfully: " + filePath);
            } else {
                Logger.warning("File not found: " + filePath);
            }
        } catch (IOException e) {
            Logger.error("Unable to delete file " + filePath + ": " + e.getMessage());
        }
    }
}
