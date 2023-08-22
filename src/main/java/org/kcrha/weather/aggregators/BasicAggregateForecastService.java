package org.kcrha.weather.aggregators;

import org.kcrha.weather.collectors.AirQualityForecastCollector;
import org.kcrha.weather.collectors.HeatRiskForecastCollector;
import org.kcrha.weather.collectors.TemperatureForecastCollector;
import org.kcrha.weather.models.BasicAggregateForecast;
import org.kcrha.weather.models.DailyAirQualityForecast;
import org.kcrha.weather.models.DailyHeatRiskForecast;
import org.kcrha.weather.models.DailyTemperatureForecast;
import org.kcrha.weather.models.metrics.AirQualityMetric;
import org.kcrha.weather.models.metrics.HeatRiskIndexMetric;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BasicAggregateForecastService implements AggregateService<BasicAggregateForecast> {
    public List<BasicAggregateForecast> getForecasts() {
        TemperatureForecastCollector temperatureForecastCollector = new TemperatureForecastCollector();
        Map<LocalDate, DailyTemperatureForecast> temperatureForecasts = temperatureForecastCollector.retrieveDailyForecasts(1)
                .stream().collect(Collectors.toMap(DailyTemperatureForecast::getDay, Function.identity()));
        AirQualityForecastCollector airQualityForecastCollector = new AirQualityForecastCollector();
        Map<LocalDate, DailyAirQualityForecast> airQualityForecasts = airQualityForecastCollector.retrieveDailyForecasts(1)
                .stream().collect(Collectors.toMap(DailyAirQualityForecast::getDay, Function.identity()));
        HeatRiskForecastCollector heatRiskForecastCollector = new HeatRiskForecastCollector();
        Map<LocalDate, DailyHeatRiskForecast> heatRiskForecasts = heatRiskForecastCollector.retrieveDailyForecasts(7)
                .stream().collect(Collectors.toMap(DailyHeatRiskForecast::getDay, Function.identity()));

        java.util.List<BasicAggregateForecast> aggregatedForecasts = new ArrayList<>();
        for (Map.Entry<LocalDate, DailyTemperatureForecast> entry : temperatureForecasts.entrySet()) {
            DailyTemperatureForecast dailyTemperatureForecast = entry.getValue();
            AirQualityMetric aqi = airQualityForecasts.getOrDefault(entry.getKey(), DailyAirQualityForecast.builder().build()).getAirQualityIndex();
            HeatRiskIndexMetric hri = heatRiskForecasts.getOrDefault(entry.getKey(), DailyHeatRiskForecast.builder().build()).getHeatRiskIndexMetric();
            aggregatedForecasts.add(BasicAggregateForecast.builder()
                    .day(dailyTemperatureForecast.getDay())
                    .airQualityIndex(aqi)
                    .temperatureLow(dailyTemperatureForecast.getTemperatureLow())
                    .temperatureAverage(dailyTemperatureForecast.getTemperatureAverage())
                    .temperatureHigh(dailyTemperatureForecast.getTemperatureHigh())
                    .heatRiskIndex(hri)
                    .build());
        }

        return aggregatedForecasts;
    }
}
