package org.kcrha.weather.models;

import lombok.*;
import org.kcrha.weather.models.metrics.HeatRiskIndexMetric;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyHeatRiskForecast implements DailyForecast {
    LocalDate day;
    HeatRiskIndexMetric heatRiskIndexMetric;

    @Override
    public LocalDate getDate() {
        return day;
    }
}
