package org.kcrha.weather.data_collectors;

import com.google.gson.*;
import org.kcrha.weather.external_api.nws.Forecast;
import org.kcrha.weather.external_api.nws.ForecastPeriod;
import org.kcrha.weather.records.DailyWeatherForecast;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WeatherForecastCollector implements ForecastCollector<DailyWeatherForecast> {
    @Override
    public List<DailyWeatherForecast> retrieveDailyForecasts(Integer days) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.weather.gov/gridpoints/SEW/125,68/forecast/hourly"))
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            return handleRequest(client.send(request, HttpResponse.BodyHandlers.ofString()));

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DailyWeatherForecast> handleRequest(HttpResponse<String> response) {
        Map<String, DailyWeatherForecast> dailyForecasts = new HashMap<>();

        Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new JsonDeserializer<ZonedDateTime>() {
            @Override
            public ZonedDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return ZonedDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME);
            }
        })
        .create();
        Forecast nwsForecast = gson.fromJson(response.body(), Forecast.class);

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        for (ForecastPeriod period : nwsForecast.properties().periods()) {
            String periodDay = formatter.format(Date.from(period.startTime().toInstant()));
            DailyWeatherForecast dailyForecast = dailyForecasts.getOrDefault(periodDay, DailyWeatherForecast.builder().day(periodDay).build());
            dailyForecast.setTemperatureLow(dailyForecast.getTemperatureLow() == null || dailyForecast.getTemperatureLow() > period.temperature() ? period.temperature() : dailyForecast.getTemperatureLow());
            dailyForecast.setTemperatureHigh(dailyForecast.getTemperatureHigh() == null || period.temperature() > dailyForecast.getTemperatureHigh() ? period.temperature() : dailyForecast.getTemperatureHigh());
            dailyForecasts.put(periodDay, dailyForecast);
        }

        return dailyForecasts.values().stream().toList();
    }
}
