package org.kcrha.weather.data_collectors;

import org.kcrha.weather.models.DailyAirQualityForecast;

import java.util.List;

public class AirQualityForecastCollector implements ForecastCollector<DailyAirQualityForecast> {
    @Override
    public List<DailyAirQualityForecast> retrieveDailyForecasts(Integer days) {
        return null;
    }
}
