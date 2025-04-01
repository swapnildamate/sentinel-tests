package org.sentinel.tests.utils;

import org.sentinel.tests.common.LoggerUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class HandleFile {

    public static void deleteDir(Path directory) {
        String userDir = System.getProperty("user.dir");
        try {
            Files.walk(Path.of(userDir + File.separator + directory))
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            LoggerUtil.warning("Unable to delete " + path + ": " + e.getMessage());
                        }
                    });
            LoggerUtil.info("Directory deleted successfully.");
        } catch (IOException e) {
            LoggerUtil.error("Error walking the file tree: " + e.getMessage());
        }
    }
}
