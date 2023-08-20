package org.kcrha.weather.collectors;

import com.google.gson.*;
import org.kcrha.weather.collectors.api.nws.Forecast;
import org.kcrha.weather.collectors.api.nws.ForecastPeriod;
import org.kcrha.weather.models.DailyTemperatureForecast;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TemperatureForecastCollector implements ForecastCollector<DailyTemperatureForecast> {
    @Override
    public List<DailyTemperatureForecast> retrieveDailyForecasts(Integer days) {
        try {
            HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).followRedirects(HttpClient.Redirect.NORMAL).build();

            HttpRequest request = HttpRequest.newBuilder().uri(new URI("https://api.weather.gov/gridpoints/SEW/125,68/forecast/hourly")).GET().timeout(Duration.ofSeconds(10)).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME)).create();

            return handleResponse(gson.fromJson(response.body(), Forecast.class));

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DailyTemperatureForecast> handleResponse(Forecast nwsForecast) {
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
                    .temperatureHigh(Collections.max(temperatures))
                    .temperatureAverage((float) temperatures.stream().reduce(0, Integer::sum) / temperatures.size())
                    .temperatureLow(Collections.min(temperatures)).build();
        }).collect(Collectors.toList());
    }
}
