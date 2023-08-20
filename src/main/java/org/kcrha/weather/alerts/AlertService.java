package org.kcrha.weather.alerts;

import org.kcrha.weather.aggregators.TemperatureAirQualityForecast;

import java.util.List;

public interface AlertService {

    public boolean sendAlert(List<TemperatureAirQualityForecast> forecasts);
}
