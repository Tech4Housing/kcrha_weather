package org.kcrha.weather;

import org.apache.commons.cli.Options;
import org.kcrha.weather.aggregators.BasicAggregateForecastService;
import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.BasicAggregateForecast;
import org.kcrha.weather.models.forecast.RegionForecast;
import org.kcrha.weather.notifications.Notification;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertService implements CommandLineService {
    private final Notification notification;

    public AlertService(Notification n) {
        notification = n;
    }

    public void run(Options options) {
        BasicAggregateForecastService forecastAggregateService = new BasicAggregateForecastService();
        List<Region> regions = RegionFileReader.getRegions();
        Map<Region, Map<LocalDate, RegionForecast>> aggregatedRegionForecasts = new HashMap<>();

        for (Region region : regions) {
            Map<LocalDate, RegionForecast> regionForecasts = aggregatedRegionForecasts.getOrDefault(region, new HashMap<>());

            for (Location location : region.locations()) {
                List<BasicAggregateForecast> forecasts = forecastAggregateService.getForecasts(7, location.lat(), location.lon());

                for (BasicAggregateForecast forecast : forecasts) {
                    // This aggregates by location instead of by day. fix thisZ
                    RegionForecast regionForecast = regionForecasts.getOrDefault(forecast.getDate(), RegionForecast.builder()
                            .temperatureHigh(forecast.getTemperatureHigh())
                            .temperatureLow(forecast.getTemperatureLow())
                            .temperatureAverage(forecast.getTemperatureAverage())
                            .airQualityIndex(forecast.getAirQualityIndex())
                            .heatRiskIndex(forecast.getHeatRiskIndex())
                            .day(forecast.getDate()).build());

//                    regionForecast.;
                }

                System.out.printf("%s \\ %s Forecast\n", region.region(), location.location());
            }


//            regionForecast.setRegion(region);

        }

//        notification.send(aggregatedForecasts);
    }
}
