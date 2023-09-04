package org.kcrha.weather.models.forecast.metrics;

import lombok.Getter;

import java.util.Map;

@Getter
public class HeatRiskIndex implements ForecastMetric {
    public final ForecastMetricType type = ForecastMetricType.HEAT_RISK_INDEX;
    public final String shortName = "HeatRisk";
    public final String longName = "HeatRisk Index";
    public Integer value;

    private final Map<Integer, String> HTML_SPANS = Map.of(
            0, spanBuilder(0, "#E3F8E1", "Green"),
            1, spanBuilder(1, "#F2F346", "Yellow"),
            2, spanBuilder(1, "#F28427", "Orange"),
            3, spanBuilder(1, "#D81827", "Red"),
            4, spanBuilder(1, "#66006C", "Magenta")
    );


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

    private String spanBuilder(Integer value, String hexColor, String colorName) {
        return String.format("<span style=\"background-color:%s\">%s (%s)</span>", hexColor, value, colorName);
    }

    public String getHtmlSpan() {
        if (value == null) {
            return "N/A";
        }
        if (HTML_SPANS.containsKey(value)) {
            return HTML_SPANS.getOrDefault(value, "N/A");
        } else {
            return String.format("N/A (Unexpected Value: %s)", value);
        }
    }
}
