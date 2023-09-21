package org.kcrha.weather.models.forecast.metrics;

import lombok.Getter;

import java.util.Map;

@Getter
public class HeatRiskIndex implements ForecastMetric {
    public final ForecastMetricType type = ForecastMetricType.HEAT_RISK_INDEX;
    public final String shortName = "HeatRisk";
    public final String longName = "HeatRisk Index";
    public Integer value;

    public static final Map<Integer, String> INDEX_COLORS = Map.of(
            0, "Green",
            1, "Yellow",
            2, "Orange",
            3, "Red",
            4, "Magenta"
    );


    public HeatRiskIndex(Integer i) {
        value = i;
    }
}
