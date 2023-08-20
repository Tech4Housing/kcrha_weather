package org.kcrha.weather.aggregators;

import org.kcrha.weather.collectors.AirQualityForecastCollector;
import org.kcrha.weather.collectors.TemperatureForecastCollector;
import org.kcrha.weather.models.BasicAggregateForecast;
import org.kcrha.weather.models.DailyAirQualityForecast;
import org.kcrha.weather.models.DailyTemperatureForecast;
import org.kcrha.weather.models.metrics.AirQualityMetric;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BasicAggregateForecastService implements AggregateService<BasicAggregateForecast> {
    public List<BasicAggregateForecast> getForecasts() {
        TemperatureForecastCollector temperatureForecastCollector = new TemperatureForecastCollector();
        Map<LocalDate, DailyTemperatureForecast> temperatureForecasts = temperatureForecastCollector.retrieveDailyForecasts(1)
                .stream().collect(Collectors.toMap(DailyTemperatureForecast::getDay, dailyTemperatureForecast -> dailyTemperatureForecast));
        AirQualityForecastCollector airQualityForecastCollector = new AirQualityForecastCollector();
        Map<LocalDate, DailyAirQualityForecast> airQualityForecasts = airQualityForecastCollector.retrieveDailyForecasts(1)
                .stream().collect(Collectors.toMap(DailyAirQualityForecast::getDay, dailyTemperatureForecast -> dailyTemperatureForecast));

        java.util.List<BasicAggregateForecast> aggregatedForecasts = new ArrayList<>();
        for (Map.Entry<LocalDate, DailyTemperatureForecast> entry : temperatureForecasts.entrySet()) {
            DailyTemperatureForecast dailyTemperatureForecast = entry.getValue();
            AirQualityMetric aqi = airQualityForecasts.getOrDefault(entry.getKey(), DailyAirQualityForecast.builder().build()).getAirQualityIndex();
            aggregatedForecasts.add(BasicAggregateForecast.builder()
                    .day(dailyTemperatureForecast.getDay())
                    .airQualityIndex(aqi)
                    .temperatureLow(dailyTemperatureForecast.getTemperatureLow())
                    .temperatureHigh(dailyTemperatureForecast.getTemperatureHigh())
                    .build());
        }

        return aggregatedForecasts;
    }
}
