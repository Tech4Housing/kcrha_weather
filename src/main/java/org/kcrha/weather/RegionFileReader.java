package org.kcrha.weather;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.kcrha.weather.models.cli.Region;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RegionFileReader {
    public static final String REGIONS_RESOURCE_FILE_PATH = "regions.json";
    public static List<Region> getRegions() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(REGIONS_RESOURCE_FILE_PATH)) {
            if (is == null) {
                throw new RuntimeException("Can't load file!");
            }
            String regionsJson = IOUtils.toString(is, StandardCharsets.UTF_8);
            Gson gson = new Gson();

            return List.of(gson.fromJson(regionsJson, Region[].class));
        } catch (IOException e) {
            throw new RuntimeException("IOException encountered while reading regions file!");
        }
    }
}
