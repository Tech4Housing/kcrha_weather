package org.kcrha.weather.models.alert;

import lombok.*;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.AggregateForecast;
import org.kcrha.weather.models.forecast.metrics.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegionForecast implements AggregateForecast {
    Region region;
    private LocalDate day;
    private AirQualityIndex airQualityIndex;
    private TemperatureHigh temperatureHigh;
    private TemperatureLow temperatureLow;
    private HeatRiskIndex heatRiskIndex;

    private List<TemperatureAverage> temperatureAverages;


    public void maxAirQualityIndex(AirQualityIndex aqi) {
        if (airQualityIndex != null && aqi != null) {
            airQualityIndex = airQualityIndex.getValue() > aqi.getValue() ? airQualityIndex : aqi;
        } else {
            airQualityIndex = airQualityIndex == null ? aqi : airQualityIndex;
        }
    }

    public void maxHeatRiskIndex(HeatRiskIndex hri) {
        if (heatRiskIndex != null && hri != null) {
            heatRiskIndex = heatRiskIndex.getValue() > hri.getValue() ? heatRiskIndex : hri;
        } else {
            heatRiskIndex = heatRiskIndex == null ? hri : heatRiskIndex;
        }

    }

    public void addTemperatureAvg(TemperatureAverage ta) {
        if (temperatureAverages == null) {
            temperatureAverages = new ArrayList<>();
        }
        temperatureAverages.add(ta);
    }

    public void maxTemperatureHigh(TemperatureHigh th) {
        temperatureHigh = temperatureHigh.getValue() > th.getValue() ? temperatureHigh : th;
    }

    public void minTemperatureLow(TemperatureLow tl) {
        temperatureLow = temperatureLow.getValue() < tl.getValue() ? temperatureLow : tl;
    }

    public TemperatureAverage getTemperatureAverage() {
        return new TemperatureAverage(temperatureAverages.stream().reduce(0, (subtotal, average) -> subtotal + average.getValue(), Integer::sum) / temperatureAverages.size());
    }

    @Override
    public LocalDate getDate() {
        return day;
    }

    @Override
    public List<ForecastMetric> getMetrics() {
        return List.of(airQualityIndex == null ? new AirQualityIndex(null) : airQualityIndex,
                temperatureHigh,
                getTemperatureAverage(),
                temperatureLow,
                heatRiskIndex == null ? new HeatRiskIndex(null) : heatRiskIndex);
    }
}
