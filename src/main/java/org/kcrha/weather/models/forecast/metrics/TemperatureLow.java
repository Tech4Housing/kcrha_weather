package org.kcrha.weather.models.forecast.metrics;

import lombok.Getter;

@Getter
public class TemperatureLow extends TemperatureBase implements ForecastMetric {
    public final ForecastMetricType type = ForecastMetricType.TEMPERATURE_LOW;
    public final String shortName = "Temp Lo";
    public final String longName = "Temperature Low";

    public TemperatureLow(Float i) {
        super(i);
    }
}
