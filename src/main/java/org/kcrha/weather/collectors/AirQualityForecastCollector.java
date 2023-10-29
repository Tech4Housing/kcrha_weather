package org.kcrha.weather.collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import lombok.AllArgsConstructor;
import org.kcrha.weather.collectors.api.airnow.Forecast;
import org.kcrha.weather.models.cli.PropertyReader;
import org.kcrha.weather.models.forecast.DailyAirQualityForecast;
import org.kcrha.weather.models.forecast.metrics.AirQualityIndex;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.IntStream;

@AllArgsConstructor
public class AirQualityForecastCollector implements ForecastCollector<DailyAirQualityForecast> {
    private HttpService client;
    private static final String API_KEY_PROPERTY = "AIRNOW_API_KEY";
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<DailyAirQualityForecast> retrieveDailyForecasts(Integer days, Float latitude, Float longitude) {
        List<DailyAirQualityForecast> forecast = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        List<String> dates = IntStream.range(0, days - 1).boxed().map(i -> dtf.format(now.plusDays(i))).toList();

        for (String date : dates) {
            try {
                HttpResponse<String> response = client.getRetryableResponse(String.format("https://www.airnowapi.org/aq/forecast/latLong/?format=application/json&date=%s&latitude=%s&longitude=%s&distance=5&API_KEY=%s", date, latitude, longitude, getApiKey()), 20);
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> LocalDate.parse(json.getAsString().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))).create();
                forecast.add(handleResponse(List.of(gson.fromJson(response.body(), Forecast[].class))));
            } catch (URISyntaxException | IOException | InterruptedException e) {
                System.out.printf("Failed to collect AirQuality (Exception: %s)\n", e);
            }
        }

        return forecast;
    }

    private DailyAirQualityForecast handleResponse(List<Forecast> forecasts) {
        return forecasts.stream().max(Comparator.comparing(Forecast::AQI)).map(forecast -> new DailyAirQualityForecast(forecast.DateForecast(), new AirQualityIndex(forecast.AQI()))).orElse(null);
    }

    private String getApiKey() {
        try {
            Properties secretProperties = PropertyReader.getSecretProperties();
            return (String) secretProperties.get(API_KEY_PROPERTY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
