package org.kcrha.weather.models.forecast.metrics;

public interface ForecastMetric {
    public ForecastMetricType getType();
    public String getShortName();
    public String getLongName();
    public String getValue();
}
