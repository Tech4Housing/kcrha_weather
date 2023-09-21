package org.kcrha.weather.aggregators;

import org.kcrha.weather.collectors.AirQualityForecastCollector;
import org.kcrha.weather.collectors.HeatRiskForecastCollector;
import org.kcrha.weather.collectors.HttpService;
import org.kcrha.weather.collectors.TemperatureForecastCollector;
import org.kcrha.weather.models.forecast.BasicAggregateForecast;
import org.kcrha.weather.models.forecast.DailyHeatRiskForecast;
import org.kcrha.weather.models.forecast.DailyTemperatureForecast;
import org.kcrha.weather.models.forecast.metrics.HeatRiskIndex;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BasicAggregateForecastService implements AggregateService<BasicAggregateForecast> {

    AirQualityForecastCollector airQualityForecastCollector;
    HeatRiskForecastCollector heatRiskForecastCollector;
    TemperatureForecastCollector temperatureForecastCollector;

    public BasicAggregateForecastService(HttpService httpService) {
        airQualityForecastCollector = new AirQualityForecastCollector(httpService);
        heatRiskForecastCollector = new HeatRiskForecastCollector(httpService);
        temperatureForecastCollector = new TemperatureForecastCollector(httpService);
    }

    public List<BasicAggregateForecast> getForecasts(Integer days, Float latitude, Float longitude) {
        Map<LocalDate, DailyTemperatureForecast> temperatureForecasts = temperatureForecastCollector.retrieveDailyForecasts(days, latitude, longitude)
                .stream().collect(Collectors.toMap(DailyTemperatureForecast::getDay, Function.identity()));
//        Map<LocalDate, DailyAirQualityForecast> airQualityForecasts = airQualityForecastCollector.retrieveDailyForecasts(days, latitude, longitude)
//                .stream().collect(Collectors.toMap(DailyAirQualityForecast::getDay, Function.identity()));
        Map<LocalDate, DailyHeatRiskForecast> heatRiskForecasts = getHeatRiskForecasts(days, latitude, longitude);
        java.util.List<BasicAggregateForecast> aggregatedForecasts = new ArrayList<>();
        for (Map.Entry<LocalDate, DailyTemperatureForecast> entry : temperatureForecasts.entrySet()) {
            DailyTemperatureForecast dailyTemperatureForecast = entry.getValue();
//            AirQualityIndex aqi = airQualityForecasts.getOrDefault(entry.getKey(), DailyAirQualityForecast.builder().build()).getAirQualityIndex();
            HeatRiskIndex hri = heatRiskForecasts.getOrDefault(entry.getKey(), DailyHeatRiskForecast.builder().build()).getHeatRiskIndex();
            aggregatedForecasts.add(BasicAggregateForecast.builder()
                    .day(dailyTemperatureForecast.getDay())
//                    .airQualityIndex(aqi)
                    .temperatureLow(dailyTemperatureForecast.getTemperatureLow())
                    .temperatureAverage(dailyTemperatureForecast.getTemperatureAverage())
                    .temperatureHigh(dailyTemperatureForecast.getTemperatureHigh())
                    .heatRiskIndex(hri)
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
