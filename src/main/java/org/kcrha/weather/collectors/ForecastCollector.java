package org.kcrha.weather.collectors;

import org.kcrha.weather.models.forecast.DailyForecast;

import java.util.List;

public interface ForecastCollector<T extends DailyForecast> {
    public List<T> retrieveDailyForecasts(Integer days, Float latitude, Float longitude);
}
