package org.kcrha.weather;

import org.apache.commons.cli.CommandLine;
import org.kcrha.weather.aggregators.BasicAggregateForecastService;
import org.kcrha.weather.collectors.HttpService;
import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.DailyAggregateForecast;
import org.kcrha.weather.notifications.ConsoleNotification;
import org.kcrha.weather.notifications.ForecastHtmlReportFormatter;
import org.kcrha.weather.notifications.Notification;
import org.kcrha.weather.notifications.ReportFormatter;

import java.util.List;
import java.util.Set;

public class ForecastService implements CommandLineService {
    private final ConsoleNotification notification;
    private final ReportFormatter formatter;
    private final HttpService httpService;

    public ForecastService() {
        this.notification = new ConsoleNotification();
        this.formatter = new ForecastHtmlReportFormatter();
        this.httpService = new HttpService();
    }

    public void run(CommandLine taskCommand) {
        List<Region> regions = RegionFileReader.getRegions();
        StringBuilder output = new StringBuilder(formatter.formatReportHeader());
        BasicAggregateForecastService forecastAggregateService = new BasicAggregateForecastService(httpService);

        for (Region region : regions) {
            for (Location location : region.locations()) {
                List<DailyAggregateForecast> aggregatedForecasts = forecastAggregateService.getForecasts(7, location.lat(), location.lon());

                output.append(formatter.formatForecastTableHeader(String.format("%s (%s)", location.location(), region.region())));
                output.append(formatter.formatForecastTable(aggregatedForecasts));
                output.append(formatter.formatForecastTableFooter(String.format("<a target=\"_blank\" href=\"https://maps.google.com/?q=%s,%s\"><i class=\"fa fa-external-link\"></i></a>\n", location.lat(), location.lon())));
            }
        }
        output.append(formatter.formatReportFooter());
        notification.send(output.toString());
    }
}
