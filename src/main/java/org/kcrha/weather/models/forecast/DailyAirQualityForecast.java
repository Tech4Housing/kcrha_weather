package org.kcrha.weather.models.forecast;

import lombok.*;
import org.kcrha.weather.models.forecast.metrics.AirQualityIndex;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyAirQualityForecast implements DailyForecast {
    LocalDate day;
    AirQualityIndex airQualityIndex;

    @Override
    public LocalDate getDate() {
        return day;
    }
}
