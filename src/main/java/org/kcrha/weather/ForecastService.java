package org.kcrha.weather;

import org.apache.commons.cli.Options;
import org.kcrha.weather.aggregators.BasicAggregateForecastService;
import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.BasicAggregateForecast;
import org.kcrha.weather.notifications.HtmlForecastNotificationFormatter;
import org.kcrha.weather.notifications.Notification;
import org.kcrha.weather.notifications.NotificationFormatter;

import java.util.List;

public class ForecastService implements CommandLineService {
    private final Notification notification;
    private final NotificationFormatter formatter;

    public ForecastService(Notification notification) {
        this(notification, null);
    }
    public ForecastService(Notification notification, NotificationFormatter formatter) {
        this.notification = notification;
        this.formatter = formatter != null ? formatter : new HtmlForecastNotificationFormatter();
    }

    public void run(Options options) {
        List<Region> regions = RegionFileReader.getRegions();
        StringBuilder output = new StringBuilder(formatter.formatHeader());

        for (Region region : regions) {
            BasicAggregateForecastService forecastAggregateService = new BasicAggregateForecastService();

            for (Location location : region.locations()) {

                List<BasicAggregateForecast> aggregatedForecasts = forecastAggregateService.getForecasts(7, location.lat(), location.lon());

                output.append(formatter.formatForecastTableHeader(String.format("%s (%s)", location.location(), region.region())));
                output.append(formatter.formatForecastTable(aggregatedForecasts));
                output.append(formatter.formatForecastTableFooter(String.format("<a target=\"_blank\" href=\"https://maps.google.com/?q=%s,%s\"><i class=\"fa fa-external-link\"></i></a>\n", location.lat(), location.lon())));
            }
        }
        output.append(formatter.formatFooter());
        notification.send(output.toString());
    }
}
