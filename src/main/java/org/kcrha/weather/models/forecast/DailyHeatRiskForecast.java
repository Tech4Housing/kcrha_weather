package org.kcrha.weather.models.forecast;

import lombok.*;
import org.kcrha.weather.models.forecast.metrics.HeatRiskIndex;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyHeatRiskForecast implements DailyForecast {
    LocalDate day;
    HeatRiskIndex heatRiskIndex;

    @Override
    public LocalDate getDate() {
        return day;
    }
}
