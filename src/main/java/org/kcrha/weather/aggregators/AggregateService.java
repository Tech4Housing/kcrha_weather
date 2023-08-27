package org.kcrha.weather.aggregators;

import org.kcrha.weather.models.forecast.AggregateForecast;

import java.util.List;

public interface AggregateService<T extends AggregateForecast> {
    public List<T> getForecasts(Integer days, Float latitude, Float longitude);
}
