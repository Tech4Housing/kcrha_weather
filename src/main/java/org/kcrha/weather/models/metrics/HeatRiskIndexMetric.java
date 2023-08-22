package org.kcrha.weather.models.metrics;

import lombok.Getter;

@Getter
public class HeatRiskIndexMetric implements ForecastMetric {
    public final ForecastMetricType type = ForecastMetricType.HEAT_RISK_INDEX;
    public final String shortName = "HeatRisk";
    public final String longName = "HeatRisk Index";
    public Integer value;

    public HeatRiskIndexMetric(Integer i) {
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
