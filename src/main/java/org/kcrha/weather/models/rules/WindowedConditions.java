package org.kcrha.weather.models.rules;

import org.kcrha.weather.models.forecast.AggregateForecast;
import org.kcrha.weather.models.forecast.metrics.ForecastMetric;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record WindowedConditions(int window, MultiConditionMatchType type, Conditions conditions) {

    public Set<LocalDate> getMatchingDatesForForecasts(List<? extends AggregateForecast> forecasts) {
        Set<LocalDate> matchingDates = new HashSet<>();

        if (forecasts.size() < window) {
            return matchingDates;
        }

        forecasts.sort(Comparator.comparing(AggregateForecast::getDate));

        for (int i = 0; i + window < forecasts.size(); i++) {
            List<? extends AggregateForecast> forecastWindow = forecasts.subList(i, i + window - 1);
            if (forecastWindow
                    .stream()
                    .map(forecast -> checkConditions(forecast.getMetrics()))
                    .collect(Collectors.toSet())
                    .contains(false)) {
                matchingDates.addAll(forecastWindow.stream().map(AggregateForecast::getDate).toList());
            }
        }
        return matchingDates;
    }

    private boolean checkConditions(List<ForecastMetric> metrics) {
        return switch (type) {
            case any -> conditions.anyConditionMatches(metrics);
            case all -> conditions.allConditionsMatch(metrics);
        };
    }
}
