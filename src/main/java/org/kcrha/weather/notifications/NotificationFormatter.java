package org.kcrha.weather.notifications;

import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.AggregateForecast;

import java.util.List;

public interface NotificationFormatter {
    public String formatHeader();
    public String formatRegionLocation(Region region, Location location);
    public String formatForecasts(List<? extends AggregateForecast> forecasts);
    public String formatFooter();
}
