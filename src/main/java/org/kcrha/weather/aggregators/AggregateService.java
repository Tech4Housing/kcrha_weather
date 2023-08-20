package org.kcrha.weather.aggregators;

import java.util.List;

public interface AggregateService<T> {
    public List<T> getForecasts();
}
