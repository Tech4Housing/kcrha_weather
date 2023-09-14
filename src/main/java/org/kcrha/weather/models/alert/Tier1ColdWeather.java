package org.kcrha.weather.models.alert;

import org.kcrha.weather.models.forecast.AggregateForecast;
import org.kcrha.weather.models.forecast.metrics.ForecastMetric;
import org.kcrha.weather.models.forecast.metrics.TemperatureHigh;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Tier1ColdWeather implements AlertRule {
    public Set<LocalDate> getDatesMatchingRule(List<? extends AggregateForecast> forecasts) {
        Set<LocalDate> matchingDates = new HashSet<>();
        if (forecasts.size() < 3) {
            return matchingDates;
        }
        forecasts.sort(Comparator.comparing(AggregateForecast::getDate));
        for (int i = 2; i < forecasts.size(); i++) {
            List<AggregateForecast> window = List.of(forecasts.get(i-2), forecasts.get(i-1), forecasts.get(i));
            if (window.stream().map(AggregateForecast::getMetrics).flatMap(List::stream).filter(f -> f instanceof TemperatureHigh).map(f -> ((TemperatureHigh) f).getFloatValue()).reduce(Float::min).orElse(46f) <= 45f) {
                matchingDates.addAll(window.stream().map(AggregateForecast::getDate).toList());
            }
        }
        return matchingDates;
    }
}
