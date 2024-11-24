package com.saleshalal.SEProject.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper class for the default logger to improve readability.
 */
public class CustomLogger {
    private final Logger logger;

    public CustomLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    /**
     * Custom Info Log
     * @param message
     */
    public void info(String message) {
        logger.info("[Custom Info Log] " + message);
    }

    /**
     * Custom warning log
     * @param message
     */
    public void warn(String message) {
        logger.warn("[Custom Warning Log] " + message);
    }

    /**
     *  Custom error log
     *  @param message
     */
    public void error(String message) {
        logger.error("[Custom Error Log] " + message);
    }

    /**
     * Custom debug log
     * @param message
     */
    public void debug(String message) {
        logger.debug("[Custom Debug Log] " + message);
    }

    public void info(String s, Object... args) {
        logger.info("[Custom Info Log] " + s, args);
    }

    public void error(String s, Object... args) {
        logger.error("[Custom Error Log] " + s, args);
    }

    public void debug(String s, Object... args) {
        logger.debug("[Custom Debug Log] " + s, args);
    }

    public void warn(String s, Object... args) {
        logger.warn("[Custom Warning Log] " + s, args);
    }
}