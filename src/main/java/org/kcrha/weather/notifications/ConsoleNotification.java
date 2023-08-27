package org.kcrha.weather.notifications;

import org.kcrha.weather.models.forecast.AggregateForecast;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConsoleNotification implements Notification {

    @Override
    public boolean send(List<? extends AggregateForecast> forecasts) {
        forecasts.stream().sorted(Comparator.comparing(AggregateForecast::getDate)).forEach(dailyAggregatedForecast -> {
            String formattedMetrics = dailyAggregatedForecast.getMetrics().stream().map(forecast -> {return forecast.getShortName() + ": " + forecast.getValue();}).collect(Collectors.joining(", "));
            System.out.printf("Date: %s, %s\n", dailyAggregatedForecast.getDate(), formattedMetrics);
        });

        return true;
    }
}
