package org.kcrha.weather.models.rules;

public record RuleSet(String name, String type, TieredWindowedConditions conditions) {
}
