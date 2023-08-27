package org.kcrha.weather.collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.kcrha.weather.collectors.api.nws.ForecastPeriod;
import org.kcrha.weather.collectors.api.nws.ForecastResponse;
import org.kcrha.weather.collectors.api.nws.GridResponse;
import org.kcrha.weather.collectors.api.nws.GridResponseProperties;
import org.kcrha.weather.models.forecast.DailyForecast;
import org.kcrha.weather.models.forecast.DailyTemperatureForecast;
import org.kcrha.weather.models.forecast.metrics.TemperatureAverage;
import org.kcrha.weather.models.forecast.metrics.TemperatureHigh;
import org.kcrha.weather.models.forecast.metrics.TemperatureLow;

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
import java.util.stream.IntStream;

abstract public class BaseForecastCollector<T extends DailyForecast> implements ForecastCollector<T> {

    protected HttpResponse<String> getRetryableResponse(String url, Integer maxAttempts) throws URISyntaxException, IOException, InterruptedException {
        int attemptCount = 0;
        while (attemptCount < maxAttempts) {
            HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).followRedirects(HttpClient.Redirect.NORMAL).build();

            HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().timeout(Duration.ofSeconds(10)).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response;
            } else {
                attemptCount += 1;
            }
        }

        throw new RuntimeException(String.format("Failed to retrieve API response for %s after %s attempts", url, maxAttempts));
    }
}
