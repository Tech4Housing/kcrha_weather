package org.kcrha.weather.models.forecast.metrics;

import lombok.Getter;

@Getter
public class TemperatureHigh extends TemperatureBase implements ForecastMetric {
    public final ForecastMetricType type = ForecastMetricType.TEMPERATURE_HIGH;
    public final String shortName = "Temp Hi";
    public final String longName = "Temperature High";

    public TemperatureHigh(Float f) {
        super(f);
    }
}
