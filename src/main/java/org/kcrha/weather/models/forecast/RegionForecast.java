package org.kcrha.weather.models.forecast;

import lombok.*;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.metrics.*;

import java.time.LocalDate;
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
    private TemperatureAverage temperatureAverage;
    private TemperatureLow temperatureLow;
    private HeatRiskIndex heatRiskIndex;


    public void maxAirQualityIndex(AirQualityIndex aqi) {
        airQualityIndex = Integer.parseInt(airQualityIndex.getValue()) > Integer.parseInt(aqi.getValue()) ? airQualityIndex : aqi;
    }

    public void maxHeatRiskIndex(HeatRiskIndex hri) {
        heatRiskIndex = Integer.parseInt(heatRiskIndex.getValue()) > Integer.parseInt(hri.getValue()) ? heatRiskIndex : hri;
    }

    public void avgTemperatureAvg(TemperatureAverage ta, Integer i) {
        temperatureAverage = new TemperatureAverage((Float.parseFloat(temperatureAverage.getValue())*i) + Float.parseFloat(ta.getValue())/(i+1));
    }

    public void maxTemperatureHigh(TemperatureHigh th) {
        temperatureHigh = Float.parseFloat(temperatureHigh.getValue()) > Float.parseFloat(th.getValue()) ? temperatureHigh : th;
    }

    public void minTemperatureLow(TemperatureLow tl) {
        temperatureLow = Float.parseFloat(temperatureLow.getValue()) < Float.parseFloat(tl.getValue()) ? temperatureLow : tl;
    }

    @Override
    public LocalDate getDate() {
        return day;
    }

    @Override
    public List<ForecastMetric> getMetrics() {
        return List.of(airQualityIndex == null ? new AirQualityIndex(null) : airQualityIndex,
                temperatureHigh,
                temperatureAverage,
                temperatureLow,
                heatRiskIndex == null ? new HeatRiskIndex(null) : heatRiskIndex);
    }
}
