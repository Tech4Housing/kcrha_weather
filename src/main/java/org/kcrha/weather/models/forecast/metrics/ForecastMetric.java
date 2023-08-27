package org.kcrha.weather.models.forecast.metrics;

public interface ForecastMetric {
    ForecastMetricType getType();

    String getShortName();

    String getLongName();

    String getValue();
}
