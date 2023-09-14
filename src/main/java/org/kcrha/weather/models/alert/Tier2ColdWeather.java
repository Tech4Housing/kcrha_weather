package org.kcrha.weather.models.alert;

import org.kcrha.weather.models.forecast.AggregateForecast;
import org.kcrha.weather.models.forecast.metrics.TemperatureHigh;
import org.kcrha.weather.models.forecast.metrics.TemperatureLow;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tier2ColdWeather implements AlertRule {
    public Set<LocalDate> getDatesMatchingRule(List<? extends AggregateForecast> forecasts) {
        Set<LocalDate> matchingDates = new HashSet<>();
        if (forecasts.size() < 3) {
            return matchingDates;
        }
        forecasts.sort(Comparator.comparing(AggregateForecast::getDate));
        for (int i = 2; i < forecasts.size(); i++) {
            List<AggregateForecast> window = List.of(forecasts.get(i-2), forecasts.get(i-1), forecasts.get(i));
            if (window.stream().map(AggregateForecast::getMetrics).flatMap(List::stream).filter(f -> f instanceof TemperatureHigh).map(f -> ((TemperatureHigh) f).getFloatValue()).reduce(Float::min).orElse(41f) <= 40f
                || window.stream().map(AggregateForecast::getMetrics).flatMap(List::stream).filter(f -> f instanceof TemperatureLow).map(f -> ((TemperatureLow) f).getFloatValue()).reduce(Float::min).orElse(36f) <= 35f
                // Snow/rain accumulation will go here
            ) {
                matchingDates.addAll(window.stream().map(AggregateForecast::getDate).toList());
            }
        }
        return matchingDates;
    }
}
