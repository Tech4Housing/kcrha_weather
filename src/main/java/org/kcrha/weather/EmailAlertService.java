package org.kcrha.weather;

import org.apache.commons.cli.CommandLine;
import org.kcrha.weather.aggregators.BasicAggregateForecastService;
import org.kcrha.weather.collectors.HttpService;
import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.DailyAggregateForecast;
import org.kcrha.weather.notifications.AlertHtmlReportFormatter;
import org.kcrha.weather.notifications.EmailNotification;
import org.kcrha.weather.notifications.builders.EmailReportBuilder;
import org.simplejavamail.api.email.Email;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmailAlertService implements CommandLineService {

    private final EmailNotification notification;
    private final EmailReportBuilder builder;
    private final HttpService httpService;

    public EmailAlertService() {
        this.notification = new EmailNotification();
        this.builder = new EmailReportBuilder(new AlertHtmlReportFormatter());
        this.httpService = new HttpService();
    }

    public void run(CommandLine taskCommand) {
        List<Region> regions = RegionFileReader.getRegions();
        BasicAggregateForecastService forecastAggregateService = new BasicAggregateForecastService(httpService);

        Map<Region, Map<Location, List<DailyAggregateForecast>>> regionalForecasts = new HashMap<>();

        for (Region region : regions) {
            if (regionalForecasts.containsKey(region)) {
                System.out.println("WARNING: Regional forecasts already contains key for " + region.region() + "; overwriting");
            }
            regionalForecasts.put(region, new HashMap<>());
            for (Location location : region.locations()) {
                if (regionalForecasts.get(region).containsKey(location)) {
                    System.out.println("WARNING: Regional forecasts already contains key for " + region.region() + " & " + location.location() + "; overwriting");
                }
                regionalForecasts.get(region).put(location, forecastAggregateService.getForecasts(7, location.lat(), location.lon()));
            }
        }

        builder.setRegionalForecasts(regionalForecasts);
        Report report = builder.getReport();

        Email email = notification.prepare(report);
        notification.send(email);
    }
}
