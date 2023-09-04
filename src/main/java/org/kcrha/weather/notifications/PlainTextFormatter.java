package org.kcrha.weather.notifications;

import lombok.Getter;
import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.AggregateForecast;
import org.kcrha.weather.models.forecast.metrics.ForecastMetric;
import org.kcrha.weather.models.forecast.metrics.ForecastMetricType;
import org.kcrha.weather.models.forecast.metrics.HeatRiskIndex;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class PlainTextFormatter implements NotificationFormatter {

    @Override
    public String formatHeader() {
        return null;
    }

    @Override
    public String formatRegionLocation(Region region, Location location) {
        return String.format("%s \\ %s Forecast\n", region.region(), location.location());
    }

    public String formatForecasts(List<? extends AggregateForecast> forecasts) {
        return forecasts.stream().sorted(Comparator.comparing(AggregateForecast::getDate)).map(dailyAggregatedForecast -> {
            String formattedMetrics = dailyAggregatedForecast.getMetrics().stream().map(forecast -> {
                return forecast.getShortName() + ": " + forecast.getValue();
            }).collect(Collectors.joining(", "));
            return String.format("Date: %s, %s\n", dailyAggregatedForecast.getDate(), formattedMetrics);
        }).collect(Collectors.joining("\n"));
    }

    @Override
    public String formatFooter() {
        return null;
    }
}
