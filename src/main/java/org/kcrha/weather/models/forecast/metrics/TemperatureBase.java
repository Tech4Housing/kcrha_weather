package org.kcrha.weather.models.forecast.metrics;

import lombok.Getter;

@Getter
abstract public class TemperatureBase implements ForecastMetric {
    public Float value;

    public TemperatureBase(Float f) {
        value = f;
    }

    @Override
    public String getValue() {
        return String.valueOf(Math.round(value));
    }
}
