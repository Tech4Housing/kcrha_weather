package org.kcrha.weather.models.alert;


import org.kcrha.weather.models.forecast.AggregateForecast;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface AlertRule {
    Set<LocalDate> getDatesMatchingRule(List<? extends AggregateForecast> forecasts);
}
