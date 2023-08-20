package org.kcrha.weather.models;


import lombok.*;
import org.kcrha.weather.models.metrics.TemperatureAverage;
import org.kcrha.weather.models.metrics.TemperatureHigh;
import org.kcrha.weather.models.metrics.TemperatureLow;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyTemperatureForecast implements DailyForecast {
    public LocalDate day;
    public TemperatureHigh temperatureHigh;
    public TemperatureAverage temperatureAverage;
    public TemperatureLow temperatureLow;

    @Override
    public LocalDate getDate() {
        return day;
    }
}
