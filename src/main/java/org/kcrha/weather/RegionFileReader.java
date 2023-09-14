package org.kcrha.weather;

import com.google.gson.Gson;
import org.kcrha.weather.models.cli.Region;

import java.util.List;

public class RegionFileReader extends BaseFileReader {
    public static final String REGIONS_RESOURCE_FILE_PATH = "regions.json";
    public static List<Region> getRegions() {

        String regionsJson = RegionFileReader.readFile(REGIONS_RESOURCE_FILE_PATH);
        Gson gson = new Gson();

        return List.of(gson.fromJson(regionsJson, Region[].class));
    }
}
