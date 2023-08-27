package org.kcrha.weather.models.forecast.metrics;

import lombok.Getter;

@Getter
public class HeatRiskIndex implements ForecastMetric {
    public final ForecastMetricType type = ForecastMetricType.HEAT_RISK_INDEX;
    public final String shortName = "HeatRisk";
    public final String longName = "HeatRisk Index";
    public Integer value;

    public HeatRiskIndex(Integer i) {
        value = i;
    }

    @Override
    public String getValue() {
        if (value == null) {
            return "N/A";
        } else {
            return String.valueOf(value);
        }
    }
}
