package org.kcrha.weather.models.rules.parser;

import org.kcrha.weather.models.forecast.metrics.ForecastMetricType;
import org.kcrha.weather.models.rules.ComparisonOperation;

public record RuleCondition(ForecastMetricType metric, ComparisonOperation comparison, Integer value) {
}
