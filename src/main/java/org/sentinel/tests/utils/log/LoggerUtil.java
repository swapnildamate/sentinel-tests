package org.sentinel.tests.utils.log;

import org.sentinel.tests.utils.insights.AllureUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * A utility class for logging operations with enhanced formatting and
 * color-coding.
 * This class provides a singleton logger instance with both file and console
 * logging capabilities.
 * 
 * Key features:
 * - Thread-safe singleton logger instance
 * - Color-coded console output based on log levels
 * - File logging with timestamp and level information
 * - Support for all standard Java logging levels (FINEST to SEVERE)
 * - Integration with Allure reporting for INFO and ERROR levels
 * 
 * Console Color Coding:
 * - SEVERE: Red
 * - WARNING: Yellow
 * - INFO: Green
 * - CONFIG: Cyan
 * - FINE: Blue
 * - FINER/FINEST: Purple
 * 
 * File logs are stored in 'app.log' with a simple format: [timestamp] [level]
 * message
 * 
 * @author <a href="https://github.com/swapnildamate">Swapnil Damate</a>
 * @version 1.0
 */
public class LoggerUtil {
    private static volatile Logger logger;
    private static final Object lock = new Object();

    // ANSI Color Codes for Console Logs
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String PURPLE = "\u001B[35m";

    private LoggerUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the singleton instance of the logger.
     * If the logger is not initialized, it initializes it.
     * 
     * @return the singleton logger instance
     */
    public static Logger getLogger() {
        if (logger == null) {
            synchronized (lock) {
                if (logger == null) {
                    logger = Logger.getLogger(LoggerUtil.class.getName());
                    setupLogger();
                }
            }
        }
        return logger;
    }

    /**
     * Configures the logger with a file handler and a console handler.
     * The file handler logs messages to a file, while the console handler
     * logs messages to the console with ANSI color codes.
     */
    private static void setupLogger() {
        logger.setUseParentHandlers(false); // Disable default logging
        logger.setLevel(Level.ALL); // Log everything

        try {
            synchronized (lock) {
                if (logger.getHandlers().length == 0) {
                    // File Handler (No Colors, Single-Line Logs)
                    FileHandler fileHandler = new FileHandler("app.log", true);
                    fileHandler.setFormatter(new Formatter() {
                        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        @Override
                        public synchronized String format(LogRecord record) {
                            return String.format("[%s] [%s] %s%n",
                                    dateFormat.format(new Date(record.getMillis())),
                                    record.getLevel(),
                                    record.getMessage());
                        }
                    });
                    fileHandler.setLevel(Level.ALL);
                    logger.addHandler(fileHandler);

                    // Console Handler (With Colors)
                    ConsoleHandler consoleHandler = new ConsoleHandler();
                    consoleHandler.setFormatter(new Formatter() {
                        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        @Override
                        public synchronized String format(LogRecord record) {
                            String color = getColor(record.getLevel());
                            return String.format("%s[%s] [%s] %s%s%n",
                                    color,
                                    dateFormat.format(new Date(record.getMillis())),
                                    record.getLevel(),
                                    record.getMessage(),
                                    RESET);
                        }

                        private String getColor(Level level) {
                            if (level == Level.SEVERE)
                                return RED;
                            else if (level == Level.WARNING)
                                return YELLOW;
                            else if (level == Level.INFO)
                                return GREEN;
                            else if (level == Level.CONFIG)
                                return CYAN;
                            else if (level == Level.FINE)
                                return BLUE;
                            else if (level == Level.FINER || level == Level.FINEST)
                                return PURPLE;
                            else
                                return RESET;
                        }
                    });
                    consoleHandler.setLevel(Level.ALL);
                    logger.addHandler(consoleHandler);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs a message at the FINEST level.
     * 
     * @param message the message to log
     */
    public static void finest(String message) {
        log(Level.FINEST, message);
    }

    /**
     * Logs a message at the FINER level.
     * 
     * @param message the message to log
     */
    public static void finer(String message) {
        log(Level.FINER, message);
    }

    /**
     * Logs a message at the FINE level.
     * 
     * @param message the message to log
     */
    public static void fine(String message) {
        log(Level.FINE, message);
    }

    /**
     * Logs a message at the CONFIG level.
     * 
     * @param message the message to log
     */
    public static void config(String message) {
        log(Level.CONFIG, message);
    }

    /**
     * Logs a message at the INFO level.
     * 
     * @param message the message to log
     */
    public static void info(String message) {
        AllureUtil.pass(message);
        log(Level.INFO, message);
    }

    /**
     * Logs a message at the WARNING level.
     * 
     * @param message the message to log
     */
    public static void warning(String message) {
        AllureUtil.warn(message);
        log(Level.WARNING, message);
    }

    /**
     * Logs a message at the SEVERE level.
     * 
     * @param message the message to log
     */
    public static void error(String message) {
        AllureUtil.fail(message);
        log(Level.SEVERE, message);
    }

    /**
     * Logs a message at the specified log level.
     * 
     * @param level   the log level
     * @param message the message to log
     */
    private static void log(Level level, String message) {
        synchronized (lock) {
            getLogger().log(level, message);
        }
    }

}