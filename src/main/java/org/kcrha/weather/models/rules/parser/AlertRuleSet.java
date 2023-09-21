package org.kcrha.weather.models.rules.parser;

import java.util.List;

public record AlertRuleSet(String alertName, String type, List<Rule> rules) {
}
