package org.kcrha.weather.collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import lombok.AllArgsConstructor;
import org.kcrha.weather.GridPointCacheFileReader;
import org.kcrha.weather.collectors.api.nws.ForecastPeriod;
import org.kcrha.weather.collectors.api.nws.ForecastResponse;
import org.kcrha.weather.collectors.api.nws.GridResponse;
import org.kcrha.weather.collectors.api.nws.GridResponseProperties;
import org.kcrha.weather.models.cli.GridPoint;
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
            GridPoint gridPoint = getGridPoints(latitude, longitude);
            ForecastResponse forecastResponse = getForecast(gridPoint);
            return handleResponse(forecastResponse);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private GridPoint getGridPoints(Float latitude, Float longitude) throws URISyntaxException, IOException, InterruptedException {
        GridPoint gridPoint = GridPointCacheFileReader.getGridPoint(latitude, longitude);
        if (gridPoint == null) {
            System.out.printf("No cached grid point found for %s,%s; querying NWS\n", latitude, longitude);
            HttpResponse<String> response = client.getRetryableResponse(String.format("https://api.weather.gov/points/%s,%s", latitude, longitude), 10);

            Gson gson = new Gson();

            GridResponse gridResponse = gson.fromJson(response.body(), GridResponse.class);
            gridPoint = new GridPoint(gridResponse.properties().gridId(), gridResponse.properties().gridX(), gridResponse.properties().gridY());
            GridPointCacheFileReader.writeGridPoint(latitude, longitude, gridPoint);
        }

        return gridPoint;
    }

    private ForecastResponse getForecast(GridPoint gridPoint) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = client.getRetryableResponse(String.format("https://api.weather.gov/gridpoints/%s/%s,%s/forecast/hourly", gridPoint.gridId(), gridPoint.x(), gridPoint.y()), 20);

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
