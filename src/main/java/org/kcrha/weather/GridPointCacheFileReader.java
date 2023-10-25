package org.kcrha.weather;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.kcrha.weather.models.cli.GridPoint;
import org.kcrha.weather.models.cli.MissingFileException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Map;

public class GridPointCacheFileReader extends BaseFileReader {
    public static final String GRID_POINTS_RESOURCE_FILE_PATH = "grid_points_cache.json";
    private static Map<String, GridPoint> gridPoints;
    private static final Type MAP_TYPE = new TypeToken<Map<String, GridPoint>>() {
    }.getType();


    public static GridPoint getGridPoint(float lat, float lon) {
        if (gridPoints == null) {
            String regionsJson = readFile(GRID_POINTS_RESOURCE_FILE_PATH);
            Gson gson = new Gson();

            gridPoints = gson.fromJson(regionsJson, MAP_TYPE);
        }

        return gridPoints.getOrDefault(String.format("%s,%s", lat, lon), null);
    }

    public static void writeGridPoint(float lat, float lon, GridPoint gridPoint) {
        String regionsJson = readFile(GRID_POINTS_RESOURCE_FILE_PATH);
        Gson gson = new Gson();

        gridPoints = gson.fromJson(regionsJson, MAP_TYPE);
        gridPoints.put(String.format("%s,%s", lat, lon), gridPoint);

        writeFile(GRID_POINTS_RESOURCE_FILE_PATH, gson.toJson(gridPoints));
    }
}
