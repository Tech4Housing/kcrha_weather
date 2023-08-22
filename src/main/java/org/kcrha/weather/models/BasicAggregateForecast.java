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
    HeatRiskIndexMetric heatRiskIndex;

    @Override
    public LocalDate getDate() {
        return day;
    }

    @Override
    public List<ForecastMetric> getMetrics() {
        return List.of(airQualityIndex == null ? new AirQualityMetric(null) : airQualityIndex,
                temperatureHigh,
                temperatureAverage,
                temperatureLow,
                heatRiskIndex == null ? new HeatRiskIndexMetric(null) : heatRiskIndex);
    }
}
