package org.kcrha.weather.collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import lombok.AllArgsConstructor;
import org.kcrha.weather.collectors.api.airnow.Forecast;
import org.kcrha.weather.models.forecast.DailyAirQualityForecast;
import org.kcrha.weather.models.forecast.metrics.AirQualityIndex;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
public class AirQualityForecastCollector implements ForecastCollector<DailyAirQualityForecast> {
    private HttpService client;

    @Override
    public List<DailyAirQualityForecast> retrieveDailyForecasts(Integer days, Float latitude, Float longitude) {
        try {
            HttpResponse<String> response = client.getRetryableResponse(String.format("https://www.airnowapi.org/aq/forecast/latLong/?format=application/json&latitude=%s&longitude=%s&distance=25&API_KEY=***REMOVED***", latitude, longitude), 10);
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> LocalDate.parse(json.getAsString().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))).create();

            return handleResponse(List.of(gson.fromJson(response.body(), Forecast[].class)));

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DailyAirQualityForecast> handleResponse(List<Forecast> forecasts) {
        return forecasts.stream().map(forecast -> new DailyAirQualityForecast(forecast.DateForecast(), new AirQualityIndex(forecast.AQI()))).toList();
    }
}
