package org.kcrha.weather;

import org.apache.commons.io.IOUtils;
import org.kcrha.weather.models.cli.MissingFileException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

abstract public class BaseFileReader {
    protected static String readFile(String filePath) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(filePath)) {
            if (is == null) {
                throw new MissingFileException(String.format("Can't load file! File: %s", filePath));
            }
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(String.format("IOException encountered while reading %s file!", filePath));
        }
    }

    protected static void writeFile(String filePath, String contents) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of(classloader.getResource(filePath).getPath()))) {
            bufferedWriter.write(contents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
