package org.kcrha.weather.models.forecast.metrics;

public interface ForecastMetric<T extends Number> {
    ForecastMetricType getType();

    String getShortName();

    String getLongName();

    T getValue();
}
