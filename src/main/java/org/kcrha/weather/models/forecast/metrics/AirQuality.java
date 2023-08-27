package org.kcrha.weather.models.forecast.metrics;

import lombok.Getter;

@Getter
public class AirQuality implements ForecastMetric {
    public final ForecastMetricType type = ForecastMetricType.AIR_QUALITY_INDEX;
    public final String shortName = "AQI";
    public final String longName = "Air Quality Index (AQI)";
    public Integer value;

    public AirQuality(Integer i) {
        value = i;
    }

    @Override
    public String getValue() {
        if (value == null) {
            return "N/A";
        } else {
            return String.valueOf(value);
        }
    }
}
