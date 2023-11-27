package org.kcrha.weather.notifications;

import lombok.Getter;
import org.apache.commons.text.WordUtils;
import org.kcrha.weather.EmailCssFileReader;
import org.kcrha.weather.models.forecast.AggregateForecast;
import org.kcrha.weather.models.forecast.metrics.ForecastMetric;
import org.kcrha.weather.models.forecast.metrics.ForecastMetricType;
import org.kcrha.weather.models.forecast.metrics.HeatRiskIndex;
import org.kcrha.weather.models.rules.RuleSet;
import org.kcrha.weather.models.rules.parser.RulesFileReader;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class HtmlAlertNotificationFormatter implements NotificationFormatter {

    public String formatHeader() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<title>Forecasts</title>\n" +
                "<style>\n" +
                EmailCssFileReader.getStyle() + "\n" +
                "</style></head><body>\n";
    }

    @Override
    public String formatForecastTableHeader(String tableHeader) {
        return String.format("\n<span class=\"forecastTableHeader\">%s</span>\n", tableHeader);
    }

    public String formatForecastTable(List<? extends AggregateForecast> forecasts) {
        StringBuilder html = new StringBuilder("<table>\n");

        List<RuleSet> ruleSets = RulesFileReader.getRules();
        Map<LocalDate, List<String>> daysWithActiveAlerts = RulesFileReader.getDatesWithActiveAlerts(ruleSets, forecasts);

        Map<LocalDate, List<? extends ForecastMetric>> dataByDate = forecasts.stream().collect(Collectors.toMap(AggregateForecast::getDate, AggregateForecast::getMetrics));
        List<LocalDate> dateHeaders = dataByDate.keySet().stream().sorted(LocalDate::compareTo).toList();
        Set<ForecastMetricType> metricsSet = new HashSet<>();
        forecasts.forEach(f -> f.getMetrics().forEach(m -> metricsSet.add(m.getType())));

        List<ForecastMetricType> orderedMetrics = metricsSet.stream().sorted().toList();

        html.append("<tr><th>Metric</th><th>");
        html.append(dateHeaders.stream().map(LocalDate::toString).collect(Collectors.joining("</th><th>")));
        html.append("</th></tr>\n");

        for (ForecastMetricType orderedMetric : orderedMetrics) {
            html.append(String.format("<tr><td>%s</td>", WordUtils.capitalize(orderedMetric.toString().replace("_", " ").toLowerCase())));


            for (LocalDate date : dateHeaders) {
                boolean foundMetric = false;
                if (daysWithActiveAlerts.containsKey(date)) {
                    html.append(String.format("<td class=\"%s\">", String.join(" ", daysWithActiveAlerts.get(date))));
                } else {
                    html.append("<td class=\"noAlert\">");
                }
                List<? extends ForecastMetric> metrics = dataByDate.get(date);

                for (ForecastMetric metric : metrics) {
                    if (metric.getType() == orderedMetric && metric.getValue() != null) {
                        if (metric instanceof HeatRiskIndex) {
                            html.append(((HeatRiskIndex) metric).getFormattedValue());
                        } else {
                            html.append(metric.getValue());
                        }

                        foundMetric = true;
                    }
                }
                if (!foundMetric) {
                    html.append("-");
                }
                html.append("</td>");
            }

            html.append("</tr>\n");
        }

        html.append("</table>\n");
        return html.toString();
    }

    @Override
    public String formatForecastTableFooter(String tableFooter) {
        return String.format("\n<span class=\"forecastTableFooter\">%s</span>\n", tableFooter);
    }

    @Override
    public String formatFooter() {
        return "</body></html>";
    }
}
