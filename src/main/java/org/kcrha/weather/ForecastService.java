package org.kcrha.weather;

import com.google.gson.Gson;
import org.apache.commons.cli.Options;
import org.apache.commons.io.IOUtils;
import org.kcrha.weather.aggregators.BasicAggregateForecastService;
import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.BasicAggregateForecast;
import org.kcrha.weather.notifications.Notification;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ForecastService implements CommandLineService {
    private final Notification notification;

    public ForecastService(Notification n) {
        notification = n;
    }

    public void run(Options options) {
        List<Region> regions = RegionFileReader.getRegions();

        for (Region region : regions) {
            BasicAggregateForecastService forecastAggregateService = new BasicAggregateForecastService();

            for (Location location : region.locations()) {
                List<BasicAggregateForecast> aggregatedForecasts = forecastAggregateService.getForecasts(7, location.lat(), location.lon());

                System.out.printf("%s \\ %s Forecast\n", region.region(), location.location());
                notification.send(aggregatedForecasts);
            }
        }
    }
}
