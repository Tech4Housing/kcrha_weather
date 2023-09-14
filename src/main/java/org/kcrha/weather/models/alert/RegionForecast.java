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
            airQualityIndex = Integer.parseInt(airQualityIndex.getValue()) > Integer.parseInt(aqi.getValue()) ? airQualityIndex : aqi;
        } else {
            airQualityIndex = airQualityIndex == null ? aqi : airQualityIndex;
        }
    }

    public void maxHeatRiskIndex(HeatRiskIndex hri) {
        if (airQualityIndex != null && hri != null) {
            heatRiskIndex = Integer.parseInt(heatRiskIndex.getValue()) > Integer.parseInt(hri.getValue()) ? heatRiskIndex : hri;
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
        temperatureHigh = Float.parseFloat(temperatureHigh.getValue()) > Float.parseFloat(th.getValue()) ? temperatureHigh : th;
    }

    public void minTemperatureLow(TemperatureLow tl) {
        temperatureLow = Float.parseFloat(temperatureLow.getValue()) < Float.parseFloat(tl.getValue()) ? temperatureLow : tl;
    }

    public TemperatureAverage getTemperatureAverage() {
        return new TemperatureAverage(temperatureAverages.stream().reduce(0f, (subtotal, average) -> subtotal + average.getFloatValue(), Float::sum) / temperatureAverages.size());
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
