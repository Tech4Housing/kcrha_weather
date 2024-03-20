package org.kcrha.weather.models.rules.parser;

import com.google.gson.Gson;
import org.kcrha.weather.BaseFileReader;
import org.kcrha.weather.models.forecast.AggregateForecast;
import org.kcrha.weather.models.rules.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class RulesFileReader extends BaseFileReader {
    public static final String ALERT_RULES_RESOURCE_FILE_PATH = "alertRules.json";
    public static final List<RuleSet> rules = getRules();

    public static List<RuleSet> getRules() {
        if (rules != null) {
            return rules;
        }

        String alertRulesJson = RulesFileReader.readFile(ALERT_RULES_RESOURCE_FILE_PATH);
        Gson gson = new Gson();

        List<AlertRuleSet> alertRuleSets = List.of(gson.fromJson(alertRulesJson, AlertRuleSet[].class));
        List<RuleSet> ruleSets = new ArrayList<>();

        for (AlertRuleSet ruleSet : alertRuleSets) {

            Map<Integer, WindowedConditions> windowedConditionsMap = new HashMap<>();

            for (Rule rule : ruleSet.rules()) {
                Conditions conditions = new Conditions(rule.conditions().stream().map(condition -> new AbstractMap.SimpleEntry<>(condition.metric(), new Condition(condition.comparison(), condition.value()))).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));
                WindowedConditions windowedConditions = new WindowedConditions(rule.window(), rule.type(), conditions);
                windowedConditionsMap.put(rule.tier(), windowedConditions);
            }

            ruleSets.add(new RuleSet(ruleSet.alertName(), ruleSet.type(), new TieredWindowedConditions(windowedConditionsMap)));
        }

        return ruleSets;
    }

    public static Map<LocalDate, List<String>> getDatesWithActiveAlerts(List<RuleSet> ruleSets, List<? extends AggregateForecast> forecasts) {
        Map<LocalDate, List<String>> daysWithActiveAlerts = new HashMap<>();

        for (RuleSet ruleSet : ruleSets) {
            Map<LocalDate, Integer> activatedAlerts = ruleSet.conditions().getActivatedTiersForDates(forecasts);
            activatedAlerts.forEach((date, value) -> {
                if (daysWithActiveAlerts.containsKey(date)) {
                    daysWithActiveAlerts.get(date).add(String.format("%s-%s", ruleSet.name(), value));
                } else {
                    daysWithActiveAlerts.put(date, List.of(String.format("%s-%s", ruleSet.name(), value)));
                }
            });
        }

        return daysWithActiveAlerts;
    }
}
