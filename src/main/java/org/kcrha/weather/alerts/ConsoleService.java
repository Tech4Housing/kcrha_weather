package org.kcrha.weather.alerts;

import org.kcrha.weather.aggregators.TemperatureAirQualityForecast;

import java.util.Comparator;
import java.util.List;

public class ConsoleService implements AlertService {

    @Override
    public boolean sendAlert(List<TemperatureAirQualityForecast> forecasts) {
        forecasts.stream().sorted(Comparator.comparing(TemperatureAirQualityForecast::getDay)).forEach(dailyAggregatedForecast -> {
            System.out.printf("Date: %s, Temp Low: %s, Temp High: %s, AQI: %s\n", dailyAggregatedForecast.getDay(), dailyAggregatedForecast.getTemperatureLow(), dailyAggregatedForecast.getTemperatureHigh(), dailyAggregatedForecast.getAirQualityIndex());
        });

        return true;
    }
}
