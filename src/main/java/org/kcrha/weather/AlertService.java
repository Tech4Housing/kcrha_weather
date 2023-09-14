package org.kcrha.weather;

import org.apache.commons.cli.Options;
import org.kcrha.weather.aggregators.BasicAggregateForecastService;
import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.BasicAggregateForecast;
import org.kcrha.weather.models.alert.RegionForecast;
import org.kcrha.weather.notifications.HtmlAlertNotificationFormatter;
import org.kcrha.weather.notifications.Notification;
import org.kcrha.weather.notifications.NotificationFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertService implements CommandLineService {
    private final Notification notification;
    private final NotificationFormatter formatter;

    public AlertService(Notification notification) {
        this(notification, null);
    }

    public AlertService(Notification notification, NotificationFormatter formatter) {
        this.notification = notification;
        this.formatter = formatter != null ? formatter : new HtmlAlertNotificationFormatter();
    }

    public void run(Options options) {
        List<Region> regions = RegionFileReader.getRegions();
        StringBuilder output = new StringBuilder(formatter.formatHeader());

        for (Region region : regions) {
            BasicAggregateForecastService forecastAggregateService = new BasicAggregateForecastService();
            Map<LocalDate, RegionForecast> dailyForecastsForRegion = new HashMap<>();

            for (Location location : region.locations()) {
                for (BasicAggregateForecast forecast : forecastAggregateService.getForecasts(7, location.lat(), location.lon())) {
                    RegionForecast dailyRegionForecast = dailyForecastsForRegion.getOrDefault(forecast.getDate(), RegionForecast.builder()
                            .day(forecast.getDate())
                            .airQualityIndex(forecast.getAirQualityIndex())
                            .temperatureHigh(forecast.getTemperatureHigh())
                            .temperatureLow(forecast.getTemperatureLow())
                            .heatRiskIndex(forecast.getHeatRiskIndex())
                            .build());
                    dailyRegionForecast.maxAirQualityIndex(forecast.getAirQualityIndex());
                    dailyRegionForecast.maxTemperatureHigh(forecast.getTemperatureHigh());
                    dailyRegionForecast.addTemperatureAvg(forecast.getTemperatureAverage());
                    dailyRegionForecast.minTemperatureLow(forecast.getTemperatureLow());
                    dailyRegionForecast.maxHeatRiskIndex(forecast.getHeatRiskIndex());
                    dailyForecastsForRegion.put(forecast.getDate(), dailyRegionForecast);
                }
            }

            output.append(formatter.formatForecastTableHeader(region.region()));
            output.append(formatter.formatForecastTable(new ArrayList<>(dailyForecastsForRegion.values())));
        }
        output.append(formatter.formatFooter());
        notification.send(output.toString());
    }
}
