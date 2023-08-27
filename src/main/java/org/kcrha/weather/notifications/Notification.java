package org.kcrha.weather.notifications;

import org.kcrha.weather.models.forecast.AggregateForecast;

import java.util.List;

public interface Notification {

    boolean send(List<? extends AggregateForecast> forecasts);
}
