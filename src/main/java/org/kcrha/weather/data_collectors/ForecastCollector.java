package org.kcrha.weather.data_collectors;

import java.util.List;

public interface ForecastCollector<T> {
    public List<T> retrieveDailyForecasts(Integer days);
}
