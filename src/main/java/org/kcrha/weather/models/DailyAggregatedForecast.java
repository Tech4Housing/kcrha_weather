package org.kcrha.weather.models;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyAggregatedForecast {
    LocalDate day;
    Integer airQualityIndex;
    public Integer temperatureHigh;
    public Integer temperatureLow;
}
