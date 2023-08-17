package org.kcrha.weather.data_collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.kcrha.weather.external_api.airnow.Forecast;
import org.kcrha.weather.models.DailyAirQualityForecast;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;

public class AirQualityForecastCollector implements ForecastCollector<DailyAirQualityForecast> {
    @Override
    public List<DailyAirQualityForecast> retrieveDailyForecasts(Integer days) {
        try {
            HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).followRedirects(HttpClient.Redirect.NORMAL).build();

            HttpRequest request = HttpRequest.newBuilder().uri(new URI("https://www.airnowapi.org/aq/forecast/latLong/?format=application/json&latitude=47.6041&longitude=-122.3282&distance=25&API_KEY=***REMOVED***")).GET().timeout(Duration.ofSeconds(10)).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> LocalDate.parse(json.getAsString().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))).create();

            return handleRequest(List.of(gson.fromJson(response.body(), Forecast[].class)));

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DailyAirQualityForecast> handleRequest(List<Forecast> forecasts) {

        return forecasts.stream().map(forecast -> new DailyAirQualityForecast(forecast.DateForecast(), forecast.AQI())).toList();
    }
}
