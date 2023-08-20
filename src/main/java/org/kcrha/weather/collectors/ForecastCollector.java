package org.kcrha.weather.collectors;

import java.util.List;

public interface ForecastCollector<DailyForecast> {
    public List<DailyForecast> retrieveDailyForecasts(Integer days);
}
