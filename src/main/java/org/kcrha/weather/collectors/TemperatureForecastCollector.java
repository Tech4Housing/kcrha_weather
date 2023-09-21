package org.kcrha.weather.collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import lombok.AllArgsConstructor;
import org.kcrha.weather.collectors.api.nws.ForecastPeriod;
import org.kcrha.weather.collectors.api.nws.ForecastResponse;
import org.kcrha.weather.collectors.api.nws.GridResponse;
import org.kcrha.weather.collectors.api.nws.GridResponseProperties;
import org.kcrha.weather.models.forecast.DailyTemperatureForecast;
import org.kcrha.weather.models.forecast.metrics.TemperatureAverage;
import org.kcrha.weather.models.forecast.metrics.TemperatureHigh;
import org.kcrha.weather.models.forecast.metrics.TemperatureLow;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class TemperatureForecastCollector implements ForecastCollector<DailyTemperatureForecast> {
    private HttpService client;

    @Override
    public List<DailyTemperatureForecast> retrieveDailyForecasts(Integer days, Float latitude, Float longitude) {
        try {
            GridResponse grid = getGridPoints(latitude, longitude);
            ForecastResponse forecastResponse = getForecast(grid.properties());
            return handleResponse(forecastResponse);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private GridResponse getGridPoints(Float latitude, Float longitude) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = client.getRetryableResponse(String.format("https://api.weather.gov/points/%s,%s", latitude, longitude), 10);

        Gson gson = new Gson();

        return gson.fromJson(response.body(), GridResponse.class);
    }

    private ForecastResponse getForecast(GridResponseProperties grid) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = client.getRetryableResponse(String.format("https://api.weather.gov/gridpoints/%s/%s,%s/forecast/hourly", grid.gridId(), grid.gridX(), grid.gridY()), 10);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME)).create();

        return gson.fromJson(response.body(), ForecastResponse.class);
    }

    private List<DailyTemperatureForecast> handleResponse(ForecastResponse nwsForecast) {
        Map<LocalDate, List<Integer>> dailyTemperatures = new HashMap<>();

        for (ForecastPeriod period : nwsForecast.properties().periods()) {
            LocalDate periodDate = period.startTime();
            List<Integer> dailyTemperature = dailyTemperatures.getOrDefault(periodDate, new ArrayList<>());
            dailyTemperature.add(period.temperature());
            dailyTemperatures.put(periodDate, dailyTemperature);
        }

        return dailyTemperatures.entrySet().stream().map(entry -> {
            final List<Integer> temperatures = entry.getValue();
            return DailyTemperatureForecast.builder()
                    .day(entry.getKey())
                    .temperatureHigh(new TemperatureHigh(Collections.max(temperatures)))
                    .temperatureAverage(new TemperatureAverage(Math.round(temperatures.stream().reduce(0, Integer::sum) / temperatures.size())))
                    .temperatureLow(new TemperatureLow(Collections.min(temperatures))).build();
        }).collect(Collectors.toList());
    }
}
