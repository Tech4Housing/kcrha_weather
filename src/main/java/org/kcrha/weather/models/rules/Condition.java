package org.kcrha.weather.models.rules;

import org.kcrha.weather.models.forecast.metrics.ForecastMetric;

public record Condition(ComparisonOperation comparisonType, Integer conditionValue) {
    public boolean passesCondition(ForecastMetric metric) {
        final int actualValue = metric.getValue();

        if (conditionValue == null || metric.getValue() == null) {
            throw new RuntimeException(String.format("The value (or comparison value) of the condition is null. (Condition value: %s, Actual value %s)", this.conditionValue, actualValue));
        }
        switch (comparisonType) {
            case greaterThan -> {
                return conditionValue > actualValue;
            }
            case greaterThanOrEqualTo -> {
                return conditionValue >= actualValue;
            }
            case equalTo -> {
                return conditionValue == actualValue;
            }
            case lessThanOrEqualTo -> {
                return conditionValue <= actualValue;
            }
            case lessThan -> {
                return conditionValue < actualValue;
            }
        }

        throw new RuntimeException(String.format("No known comparisonType matched. (provided: %s)", comparisonType));
    }
}
