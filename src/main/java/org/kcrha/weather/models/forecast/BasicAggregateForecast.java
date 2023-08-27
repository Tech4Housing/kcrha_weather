package org.kcrha.weather.models.forecast;

import lombok.*;
import org.kcrha.weather.models.forecast.metrics.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasicAggregateForecast implements AggregateForecast {
    LocalDate day;
    AirQuality airQualityIndex;
    TemperatureHigh temperatureHigh;
    TemperatureAverage temperatureAverage;
    TemperatureLow temperatureLow;
    HeatRiskIndex heatRiskIndex;

    @Override
    public LocalDate getDate() {
        return day;
    }

    @Override
    public List<ForecastMetric> getMetrics() {
        return List.of(airQualityIndex == null ? new AirQuality(null) : airQualityIndex,
                temperatureHigh,
                temperatureAverage,
                temperatureLow,
                heatRiskIndex == null ? new HeatRiskIndex(null) : heatRiskIndex);
    }
}
