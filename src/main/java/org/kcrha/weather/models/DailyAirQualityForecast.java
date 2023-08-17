package org.kcrha.weather.models;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyAirQualityForecast {
    LocalDate day;
    Integer airQualityIndex;
}
