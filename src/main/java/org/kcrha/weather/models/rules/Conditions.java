package org.kcrha.weather.models.rules;

import org.kcrha.weather.models.forecast.metrics.ForecastMetric;
import org.kcrha.weather.models.forecast.metrics.ForecastMetricType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record Conditions(Map<ForecastMetricType, Condition> conditionMap) {
    public boolean anyConditionMatches(List<ForecastMetric> metrics) {
        return checkConditions(metrics).containsValue(true);
    }

    public boolean allConditionsMatch(List<ForecastMetric> metrics) {
        return !checkConditions(metrics).containsValue(false);
    }

    private Map<ForecastMetricType, Boolean> checkConditions(List<ForecastMetric> metrics) {
        Map<ForecastMetricType, Boolean> areConditionsMet = conditionMap.keySet().stream().collect(Collectors.toMap(Function.identity(), c -> false));

        for (ForecastMetric metric : metrics) {
            if (conditionMap.containsKey(metric.getType())) {
                areConditionsMet.put(metric.getType(), conditionMap.get(metric.getType()).passesCondition(metric));
            }
        }

        return areConditionsMet;
    }
}
