package org.kcrha.weather.models;

import lombok.*;
import org.kcrha.weather.models.metrics.AirQualityMetric;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyAirQualityForecast implements DailyForecast {
    LocalDate day;
    AirQualityMetric airQualityIndex;

    @Override
    public LocalDate getDate() {
        return day;
    }
}
