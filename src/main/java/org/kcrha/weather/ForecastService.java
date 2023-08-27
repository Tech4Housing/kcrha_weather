package org.kcrha.weather;

import com.google.gson.Gson;
import org.apache.commons.cli.Options;
import org.apache.commons.io.IOUtils;
import org.kcrha.weather.aggregators.BasicAggregateForecastService;
import org.kcrha.weather.collectors.api.nws.HeatRisk;
import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.BasicAggregateForecast;
import org.kcrha.weather.notifications.ConsoleNotification;
import org.kcrha.weather.notifications.EmailNotification;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ForecastService implements CommandLineService {
    public static final String REGIONS_RESOURCE_FILE_PATH = "regions.json";

    public void run(Options options) {
        ConsoleNotification alertService = new ConsoleNotification();

        List<Region> regions = getRegions();

        for (Region region : regions) {
            BasicAggregateForecastService forecastAggregateService = new BasicAggregateForecastService();

            for (Location location : region.locations()) {
                List<BasicAggregateForecast> aggregatedForecasts = forecastAggregateService.getForecasts(7, location.lat(), location.lon());

                System.out.printf("%s \\ %s Forecast\n", region.region(), location.location());
                alertService.send(aggregatedForecasts);
            }
        }
    }

    private List<Region> getRegions() {
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
