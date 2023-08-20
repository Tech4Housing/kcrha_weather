package org.kcrha.weather.alerts;

import org.kcrha.weather.models.AggregateForecast;

import java.util.List;

public interface AlertService {

    public boolean sendAlert(List<AggregateForecast> forecasts);
}
