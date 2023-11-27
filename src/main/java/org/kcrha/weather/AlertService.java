package org.kcrha.weather;

import org.apache.commons.cli.CommandLine;
import org.kcrha.weather.aggregators.BasicAggregateForecastService;
import org.kcrha.weather.collectors.HttpService;
import org.kcrha.weather.models.alert.RegionForecast;
import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.BasicAggregateForecast;
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
    private final HttpService httpService;

    public AlertService(Notification notification) {
        this(notification, null, null);
    }

    public AlertService(Notification notification, NotificationFormatter formatter, HttpService httpService) {
        this.notification = notification;
        this.formatter = formatter != null ? formatter : new HtmlAlertNotificationFormatter();
        this.httpService = httpService != null ? httpService : new HttpService();
    }

    public void run(CommandLine taskCommand) {
        List<Region> regions = RegionFileReader.getRegions();
        StringBuilder output = new StringBuilder(formatter.formatHeader());
        BasicAggregateForecastService forecastAggregateService = new BasicAggregateForecastService(httpService);

        for (Region region : regions) {
            for (Location location : region.locations()) {
                List<BasicAggregateForecast> aggregatedForecasts = forecastAggregateService.getForecasts(7, location.lat(), location.lon());

                output.append(formatter.formatForecastTableHeader(String.format("%s (%s)", location.location(), region.region())));
                output.append(formatter.formatForecastTable(aggregatedForecasts));
                output.append(formatter.formatForecastTableFooter(""));
            }
        }
        notification.send(output.toString());
    }
}
