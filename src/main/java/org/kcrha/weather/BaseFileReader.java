package org.kcrha.weather;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

abstract public class BaseFileReader {
    protected static String readFile(String filePath) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(filePath)) {
            if (is == null) {
                throw new RuntimeException(String.format("Can't load file! File: %s", filePath));
            }
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(String.format("IOException encountered while reading %s file!", filePath));
        }
    }
}
