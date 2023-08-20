package org.kcrha.weather.alerts;

import org.kcrha.weather.aggregators.TemperatureAirQualityForecast;

import java.util.List;

public class MailingService implements AlertService {


    @Override
    public boolean sendAlert(List<TemperatureAirQualityForecast> forecasts) {
        return false;
    }
}
