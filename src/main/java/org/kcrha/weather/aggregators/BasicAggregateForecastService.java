package org.kcrha.weather.aggregators;

import org.kcrha.weather.collectors.AirQualityForecastCollector;
import org.kcrha.weather.collectors.HeatRiskForecastCollector;
import org.kcrha.weather.collectors.HttpService;
import org.kcrha.weather.collectors.WeatherForecastCollector;
import org.kcrha.weather.models.forecast.DailyAggregateForecast;
import org.kcrha.weather.models.forecast.DailyAirQualityForecast;
import org.kcrha.weather.models.forecast.DailyHeatRiskForecast;
import org.kcrha.weather.models.forecast.DailyWeatherForecast;
import org.kcrha.weather.models.forecast.metrics.AirQualityIndex;
import org.kcrha.weather.models.forecast.metrics.HeatRiskIndex;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BasicAggregateForecastService implements AggregateService<DailyAggregateForecast> {

    AirQualityForecastCollector airQualityForecastCollector;
    HeatRiskForecastCollector heatRiskForecastCollector;
    WeatherForecastCollector weatherForecastCollector;

    public BasicAggregateForecastService(HttpService httpService) {
        airQualityForecastCollector = new AirQualityForecastCollector(httpService);
        heatRiskForecastCollector = new HeatRiskForecastCollector(httpService);
        weatherForecastCollector = new WeatherForecastCollector(httpService);
    }

    public List<DailyAggregateForecast> getForecasts(Integer days, Float latitude, Float longitude) {
        Map<LocalDate, DailyWeatherForecast> temperatureForecasts = weatherForecastCollector.retrieveDailyForecasts(days, latitude, longitude)
                .stream().collect(Collectors.toMap(DailyWeatherForecast::getDay, Function.identity()));
        List<DailyAirQualityForecast> airQualityForecasts = airQualityForecastCollector.retrieveDailyForecasts(days, latitude, longitude);
        Map<LocalDate, DailyAirQualityForecast> airQualityForecastMap = airQualityForecasts.stream().filter(Objects::nonNull).collect(Collectors.toMap(DailyAirQualityForecast::getDay, Function.identity()));
        Map<LocalDate, DailyHeatRiskForecast> heatRiskForecasts = getHeatRiskForecasts(days, latitude, longitude);
        java.util.List<DailyAggregateForecast> aggregatedForecasts = new ArrayList<>();
        for (Map.Entry<LocalDate, DailyWeatherForecast> entry : temperatureForecasts.entrySet()) {
            DailyWeatherForecast dailyWeatherForecast = entry.getValue();
            AirQualityIndex aqi = airQualityForecastMap.getOrDefault(entry.getKey(), DailyAirQualityForecast.builder().build()).getAirQualityIndex();
            HeatRiskIndex hri = heatRiskForecasts.getOrDefault(entry.getKey(), DailyHeatRiskForecast.builder().build()).getHeatRiskIndex();
            aggregatedForecasts.add(DailyAggregateForecast.builder()
                    .day(dailyWeatherForecast.getDay())
                    .airQualityIndex(aqi)
                    .temperatureLow(dailyWeatherForecast.getTemperatureLow())
                    .temperatureAverage(dailyWeatherForecast.getTemperatureAverage())
                    .temperatureHigh(dailyWeatherForecast.getTemperatureHigh())
                    .heatRiskIndex(hri)
                    .rainfallAccumulation(dailyWeatherForecast.getRainfallAccumulation())
                    .snowIceAccumulation(dailyWeatherForecast.getSnowIceAccumulation())
                    .build());
        }

        return aggregatedForecasts;
    }

    private Map<LocalDate, DailyHeatRiskForecast> getHeatRiskForecasts(Integer days, Float latitude, Float longitude) {
        try {
            return heatRiskForecastCollector.retrieveDailyForecasts(days, latitude, longitude)
                    .stream().collect(Collectors.toMap(DailyHeatRiskForecast::getDay, Function.identity()));
        } catch (Throwable t) {
            System.out.println("Unable to fetch heat risk forecasts");
            return new HashMap<>();
        }
    }
}
