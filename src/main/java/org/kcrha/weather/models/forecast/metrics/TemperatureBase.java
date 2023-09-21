package org.kcrha.weather.models.forecast.metrics;

import lombok.Getter;

@Getter
abstract public class TemperatureBase implements ForecastMetric {
    public Integer value;

    public TemperatureBase(Integer i) {
        value = i;
    }
}
