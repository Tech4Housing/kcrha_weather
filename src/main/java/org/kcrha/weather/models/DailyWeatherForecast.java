package org.kcrha.weather.models;


import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyWeatherForecast {
    public LocalDate day;
    public Integer temperatureHigh;
    public Integer temperatureLow;
}
