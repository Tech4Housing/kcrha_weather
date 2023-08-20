package org.kcrha.weather.models.metrics;

import lombok.Getter;

@Getter
public class AirQualityMetric implements ForecastMetric {
    public final ForecastMetricType type = ForecastMetricType.AIR_QUALITY_INDEX;
    public final String shortName = "AQI";
    public final String longName = "Air Quality Index (AQI)";
    public Integer value;

    public AirQualityMetric(Integer i) {
        value = i;
    }

    @Override
    public String getValue() {
        return String.valueOf(value);
    }
}
