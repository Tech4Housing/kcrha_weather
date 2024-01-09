package org.kcrha.weather.models.forecast.metrics;

import lombok.Getter;

@Getter
public class SnowIceAccumulation implements ForecastMetric<Float> {
    public final ForecastMetricType type = ForecastMetricType.SNOW_ICE_ACCUMULATION;
    public final String shortName = "Snow/Ice";
    public final String longName = "Snow & Ice Accumulation";
    public Float value;

    public SnowIceAccumulation(Float f) {
        value = f;
    }
}
