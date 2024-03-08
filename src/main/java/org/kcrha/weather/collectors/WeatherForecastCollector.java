package org.kcrha.weather.collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import lombok.AllArgsConstructor;
import org.kcrha.weather.GridPointCacheFileReader;
import org.kcrha.weather.collectors.api.nws.hourly_forecast.GridResponse;
import org.kcrha.weather.collectors.api.nws.raw_forecast.RawForecastResponse;
import org.kcrha.weather.collectors.api.nws.raw_forecast.RawForecastValue;
import org.kcrha.weather.models.cli.GridPoint;
import org.kcrha.weather.models.forecast.DailyWeatherForecast;
import org.kcrha.weather.models.forecast.metrics.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

@AllArgsConstructor
public class WeatherForecastCollector implements ForecastCollector<DailyWeatherForecast> {
    private HttpService client;

    @Override
    public List<DailyWeatherForecast> retrieveDailyForecasts(Integer days, Float latitude, Float longitude) {
        try {
            GridPoint gridPoint = getGridPoints(latitude, longitude);
            RawForecastResponse rawForecastResponse = getForecast(gridPoint);
            return handleResponse(rawForecastResponse, days);
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

    private RawForecastResponse getForecast(GridPoint gridPoint) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = client.getRetryableResponse(String.format("https://api.weather.gov/gridpoints/%s/%s,%s", gridPoint.gridId(), gridPoint.x(), gridPoint.y()), 20);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME)).create();

        return gson.fromJson(response.body(), RawForecastResponse.class);
    }

    private List<DailyWeatherForecast> handleResponse(RawForecastResponse nwsForecast, Integer days) {

        LocalDateTime now = LocalDateTime.now();
        List<LocalDate> allowedDates = IntStream.range(0, days).boxed().map(now::plusDays).map(LocalDateTime::toLocalDate).toList();

        Map<LocalDate, List<Float>> dailyTemperatures = new HashMap<>();
        Map<LocalDate, List<Float>> dailyRainfallAccumulations = new HashMap<>();
        Map<LocalDate, List<Float>> dailySnowIceAccumulations = new HashMap<>();

        for (RawForecastValue value : nwsForecast.properties().temperature().values()) {
            LocalDate periodDate = ZonedDateTime.parse(value.validTime().split("/")[0], ISO_OFFSET_DATE_TIME).withZoneSameInstant(ZoneId.systemDefault()).toLocalDate();
            List<Float> dailyTemperature = dailyTemperatures.getOrDefault(periodDate, new ArrayList<>());
            dailyTemperature.add(value.value());
            dailyTemperatures.put(periodDate, dailyTemperature);
        }

        for (RawForecastValue value : nwsForecast.properties().quantitativePrecipitation().values()) {
            LocalDate periodDate = ZonedDateTime.parse(value.validTime().split("/")[0], ISO_OFFSET_DATE_TIME).withZoneSameInstant(ZoneId.systemDefault()).toLocalDate();
            List<Float> dailyRainfallAccumulation = dailyRainfallAccumulations.getOrDefault(periodDate, new ArrayList<>());
            dailyRainfallAccumulation.add(value.value());
            dailyRainfallAccumulations.put(periodDate, dailyRainfallAccumulation);
        }

        for (RawForecastValue value : nwsForecast.properties().iceAccumulation().values()) {
            LocalDate periodDate = ZonedDateTime.parse(value.validTime().split("/")[0], ISO_OFFSET_DATE_TIME).withZoneSameInstant(ZoneId.systemDefault()).toLocalDate();
            List<Float> dailySnowIceAccumulation = dailySnowIceAccumulations.getOrDefault(periodDate, new ArrayList<>());
            dailySnowIceAccumulation.add(value.value());
            dailySnowIceAccumulations.put(periodDate, dailySnowIceAccumulation);
        }

        for (RawForecastValue value : nwsForecast.properties().snowfallAmount().values()) {
            LocalDate periodDate = ZonedDateTime.parse(value.validTime().split("/")[0], ISO_OFFSET_DATE_TIME).withZoneSameInstant(ZoneId.systemDefault()).toLocalDate();
            List<Float> dailySnowIceAccumulation = dailySnowIceAccumulations.getOrDefault(periodDate, new ArrayList<>());
            dailySnowIceAccumulation.add(value.value());
            dailySnowIceAccumulations.put(periodDate, dailySnowIceAccumulation);
        }

        Set<LocalDate> dates = new HashSet<>();
        dates.addAll(dailyTemperatures.keySet().stream().toList());
        dates.addAll(dailyRainfallAccumulations.keySet().stream().toList());
        dates.addAll(dailySnowIceAccumulations.keySet().stream().toList());

        return dates.stream().filter(allowedDates::contains).map(date -> {
            final List<Float> temperatures = dailyTemperatures.getOrDefault(date, new ArrayList<>());
            final List<Float> rainfallAccumulations = dailyRainfallAccumulations.getOrDefault(date, new ArrayList<>());
            final List<Float> snowfallAccumulations = dailySnowIceAccumulations.getOrDefault(date, new ArrayList<>());
            return DailyWeatherForecast.builder()
                    .day(date)
                    .temperatureHigh(new TemperatureHigh(convertCelsiusToFahrenheit(Collections.max(temperatures))))
                    .temperatureAverage(new TemperatureAverage(convertCelsiusToFahrenheit(temperatures.stream().reduce(0.0f, Float::sum) / temperatures.size())))
                    .temperatureLow(new TemperatureLow(convertCelsiusToFahrenheit(Collections.min(temperatures))))
                    .rainfallAccumulation(new RainfallAccumulation(sumPrecipitation(rainfallAccumulations)))
                    .snowIceAccumulation(new SnowIceAccumulation(sumPrecipitation(snowfallAccumulations)))
                    .build();
        }).collect(Collectors.toList());
    }

    private Integer convertCelsiusToFahrenheit(Float celsius) {
        return Math.round(celsius * 1.8f) + 32;
    }

    private Float sumPrecipitation(List<Float> precipitationAmounts) {
        return Math.round(((precipitationAmounts.stream().reduce(0.0f, Float::sum)) / 25.4f) * 100) / 100f;
    }
}
