package org.kcrha.weather.models.forecast.metrics;

import lombok.Getter;

@Getter
public class AirQualityIndex implements ForecastMetric<Integer> {
    public final ForecastMetricType type = ForecastMetricType.AIR_QUALITY_INDEX;
    public final String shortName = "AQI";
    public final String longName = "Air Quality Index (AQI)";
    public Integer value;

    public AirQualityIndex(Integer i) {
        value = i;
    }
}
