package org.kcrha.weather;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.kcrha.weather.models.cli.Region;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RegionFileReader extends BaseFileReader {
    public static final String REGIONS_RESOURCE_FILE_PATH = "regions.json";
    public static List<Region> getRegions() {

        String regionsJson = RegionFileReader.readFile(REGIONS_RESOURCE_FILE_PATH);
        Gson gson = new Gson();

        return List.of(gson.fromJson(regionsJson, Region[].class));
    }
}
