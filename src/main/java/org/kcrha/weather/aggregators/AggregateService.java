package org.kcrha.weather.aggregators;

import org.kcrha.weather.models.AggregateForecast;

import java.util.List;

public interface AggregateService<T extends AggregateForecast> {
    public List<T> getForecasts();
}
