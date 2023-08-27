package org.kcrha.weather.collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.kcrha.weather.collectors.api.airnow.Forecast;
import org.kcrha.weather.models.forecast.DailyAirQualityForecast;
import org.kcrha.weather.models.forecast.metrics.AirQuality;

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

public class AirQualityForecastCollector extends BaseForecastCollector<DailyAirQualityForecast> implements ForecastCollector<DailyAirQualityForecast> {
    @Override
    public List<DailyAirQualityForecast> retrieveDailyForecasts(Integer days, Float latitude, Float longitude) {
        try {
            HttpResponse<String> response = getRetryableResponse(String.format("https://www.airnowapi.org/aq/forecast/latLong/?format=application/json&latitude=%s&longitude=%s&distance=25&API_KEY=***REMOVED***", latitude, longitude), 10);
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> LocalDate.parse(json.getAsString().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))).create();

            return handleResponse(List.of(gson.fromJson(response.body(), Forecast[].class)));

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DailyAirQualityForecast> handleResponse(List<Forecast> forecasts) {
        return forecasts.stream().map(forecast -> new DailyAirQualityForecast(forecast.DateForecast(), new AirQuality(forecast.AQI()))).toList();
    }
}
