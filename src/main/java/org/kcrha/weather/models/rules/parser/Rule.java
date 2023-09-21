package org.kcrha.weather.models.rules.parser;

import org.kcrha.weather.models.rules.MultiConditionMatchType;

import java.util.List;

public record Rule(Integer tier, MultiConditionMatchType type, Integer window, List<RuleCondition> conditions) {
}
