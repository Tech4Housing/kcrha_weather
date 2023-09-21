package org.kcrha.weather.models.forecast.metrics;

import lombok.Getter;

@Getter
public class TemperatureAverage extends TemperatureBase implements ForecastMetric {
    public final ForecastMetricType type = ForecastMetricType.TEMPERATURE_AVERAGE;
    public final String shortName = "Temp Avg";
    public final String longName = "Temperature Average";

    public TemperatureAverage(Integer i) {
        super(i);
    }
}
