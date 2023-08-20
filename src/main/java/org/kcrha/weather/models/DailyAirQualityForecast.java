package org.kcrha.weather.models;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyAirQualityForecast implements DailyForecast {
    LocalDate day;
    Integer airQualityIndex;

    @Override
    public LocalDate getDate() {
        return day;
    }
}
