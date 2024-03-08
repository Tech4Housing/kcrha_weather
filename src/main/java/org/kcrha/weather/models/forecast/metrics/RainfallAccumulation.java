package org.kcrha.weather.models.forecast.metrics;

import lombok.Getter;

@Getter
public class RainfallAccumulation implements ForecastMetric<Float> {
    public final ForecastMetricType type = ForecastMetricType.RAIN_ACCUMULATION;
    public final String shortName = "Rain";
    public final String longName = "Rainfall Accumulation";
    public Float value;

    public RainfallAccumulation(Float f) {
        value = f;
    }
}
