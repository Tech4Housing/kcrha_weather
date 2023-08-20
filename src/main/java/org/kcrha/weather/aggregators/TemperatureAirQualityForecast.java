package org.kcrha.weather.aggregators;

import lombok.*;
import org.kcrha.weather.models.DailyForecast;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemperatureAirQualityForecast implements DailyForecast {
    LocalDate day;
    Integer airQualityIndex;
    public Integer temperatureHigh;
    public Integer temperatureLow;

    @Override
    public LocalDate getDate() {
        return day;
    }
}
