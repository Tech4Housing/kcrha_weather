package org.kcrha.weather.collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import lombok.AllArgsConstructor;
import org.kcrha.weather.collectors.api.nws.HeatRisk;
import org.kcrha.weather.models.forecast.DailyHeatRiskForecast;
import org.kcrha.weather.models.forecast.metrics.HeatRiskIndex;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
public class HeatRiskForecastCollector implements ForecastCollector<DailyHeatRiskForecast> {
    private HttpService client;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public List<DailyHeatRiskForecast> retrieveDailyForecasts(Integer days, Float latitude, Float longitude) {
        try {
            LocalDateTime now = LocalDateTime.now();
            String dates = IntStream.range(0, days - 1).boxed().map(i -> dtf.format(now.plusDays(i))).collect(Collectors.joining("%2C"));

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(new URI(String.format("https://www.wrh.noaa.gov/wrh/heatrisk/php/getValsByLatLon.php?lat=%s&lon=%s&days=%s", latitude, longitude, dates)))
                    .header("Accept", "application/json, text/javascript, */*; q=0.01")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.3 Safari/605.1.15")
                    .header("Referer", "https://www.wrh.noaa.gov/wrh/heatrisk/")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = client.getResponse(request);
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> LocalDate.parse(json.getAsString().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))).create();

            return handleResponse(List.of(gson.fromJson(response.body(), HeatRisk[].class)));

        } catch (URISyntaxException | IOException | InterruptedException | IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DailyHeatRiskForecast> handleResponse(List<HeatRisk> heatRiskForecasts) {

        return heatRiskForecasts.stream().map(heatRiskForecast -> new DailyHeatRiskForecast(LocalDate.parse(heatRiskForecast.date(), dtf), new HeatRiskIndex(Integer.parseInt(heatRiskForecast.heatrisk().replace(".0", ""))))).toList();
    }
}
