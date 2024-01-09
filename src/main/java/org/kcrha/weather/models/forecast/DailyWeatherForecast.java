package org.kcrha.weather.models.forecast;


import lombok.*;
import org.kcrha.weather.models.forecast.metrics.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyWeatherForecast implements DailyForecast {
    public LocalDate day;
    public TemperatureHigh temperatureHigh;
    public TemperatureAverage temperatureAverage;
    public TemperatureLow temperatureLow;
    public RainfallAccumulation rainfallAccumulation;
    public SnowIceAccumulation snowIceAccumulation;


    @Override
    public LocalDate getDate() {
        return day;
    }
}
