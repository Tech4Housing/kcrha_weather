package org.kcrha.weather.models;


import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyTemperatureForecast implements DailyForecast {
    public LocalDate day;
    public Integer temperatureHigh;
    public Float temperatureAverage;
    public Integer temperatureLow;

    @Override
    public LocalDate getDate() {
        return day;
    }
}
