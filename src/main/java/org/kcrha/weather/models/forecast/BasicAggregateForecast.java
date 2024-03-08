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
    private LocalDate day;
    private AirQualityIndex airQualityIndex;
    private TemperatureHigh temperatureHigh;
    private TemperatureAverage temperatureAverage;
    private TemperatureLow temperatureLow;
    private HeatRiskIndex heatRiskIndex;
    private RainfallAccumulation rainfallAccumulation;
    private SnowIceAccumulation snowIceAccumulation;

    @Override
    public LocalDate getDate() {
        return day;
    }

    @Override
    public List<ForecastMetric<?>> getMetrics() {
        return List.of(airQualityIndex == null ? new AirQualityIndex(null) : airQualityIndex,
                temperatureHigh,
                temperatureAverage,
                temperatureLow,
                heatRiskIndex == null ? new HeatRiskIndex(null) : heatRiskIndex,
                rainfallAccumulation == null ? new RainfallAccumulation(null) : rainfallAccumulation,
                snowIceAccumulation == null ? new SnowIceAccumulation(null) : snowIceAccumulation);
    }
}
