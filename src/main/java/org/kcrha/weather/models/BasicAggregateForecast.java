package org.kcrha.weather.models;

import lombok.*;
import org.kcrha.weather.models.metrics.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasicAggregateForecast implements AggregateForecast {
    LocalDate day;
    AirQualityMetric airQualityIndex;
    TemperatureHigh temperatureHigh;
    TemperatureAverage temperatureAverage;
    TemperatureLow temperatureLow;

    @Override
    public LocalDate getDate() {
        return day;
    }

    @Override
    public List<ForecastMetric> getMetrics() {
        return List.of(airQualityIndex, temperatureHigh, temperatureAverage, temperatureLow);
    }
}
