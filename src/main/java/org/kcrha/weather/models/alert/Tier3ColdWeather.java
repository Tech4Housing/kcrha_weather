package org.kcrha.weather.models.alert;

import org.kcrha.weather.models.forecast.AggregateForecast;
import org.kcrha.weather.models.forecast.metrics.TemperatureHigh;
import org.kcrha.weather.models.forecast.metrics.TemperatureLow;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tier3ColdWeather implements AlertRule {
    public Set<LocalDate> getDatesMatchingRule(List<? extends AggregateForecast> forecasts) {
        Set<LocalDate> matchingDates = new HashSet<>();
        for (AggregateForecast forecast : forecasts) {
            if (forecast.getMetrics().stream().filter(f -> f instanceof TemperatureHigh).map(f -> ((TemperatureHigh) f).getFloatValue()).reduce(Float::min).orElse(36f) <= 35f
                || forecast.getMetrics().stream().filter(f -> f instanceof TemperatureLow).map(f -> ((TemperatureLow) f).getFloatValue()).reduce(Float::min).orElse(31f) <= 30f
                // Snow/rain accumulation will go here
            ) {
                matchingDates.add(forecast.getDate());
            }
        }
        return matchingDates;
    }}
