package org.kcrha.weather.notifications;

import lombok.Getter;
import org.apache.commons.text.WordUtils;
import org.kcrha.weather.EmailCssFileReader;
import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.AggregateForecast;
import org.kcrha.weather.models.forecast.metrics.ForecastMetric;
import org.kcrha.weather.models.forecast.metrics.ForecastMetricType;
import org.kcrha.weather.models.forecast.metrics.HeatRiskIndex;
import org.kcrha.weather.models.rules.RuleSet;
import org.kcrha.weather.models.rules.parser.RulesFileReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class AlertHtmlReportFormatter implements ReportFormatter {

    public String formatReportHeader() {
        return formatReportHeader(null);
    }

    public String formatReportHeader(Map<Region, Map<Location, Map<LocalDate, List<String>>>> allActiveAlerts) {
        String activeAlertSummary = "<h3>No active alerts</h3>";

        if (allActiveAlerts != null && !allActiveAlerts.isEmpty()) {
            StringBuilder builder = new StringBuilder();

            final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd");

            builder.append("<span class=\"forecastTableHeader\">Executive Summary</span><table><tr><th>Region / Location</th><th>Type (Severity)</th><th>Dates</th></tr>");
            for (Map.Entry<Region, Map<Location, Map<LocalDate, List<String>>>> region : allActiveAlerts.entrySet()) {
                for (Map.Entry<Location, Map<LocalDate, List<String>>> location : region.getValue().entrySet()) {
                    Map<String, List<LocalDate>> severityForDays = new HashMap<>();
                    for (Map.Entry<LocalDate, List<String>> activeAlerts : location.getValue().entrySet()) {
                        for (String activeAlert : activeAlerts.getValue()) {
                            if (!severityForDays.containsKey(activeAlert)) {
                                severityForDays.put(activeAlert, new ArrayList<>());
                            }
                            severityForDays.get(activeAlert).add(activeAlerts.getKey());
                        }
                    }
                    for (Map.Entry<String, List<LocalDate>> severity : severityForDays.entrySet()) {
                        String sev = WordUtils.capitalize(severity.getKey().split("-")[0]) + " (" + severity.getKey().split("-")[1] + ")";
                        builder.append("<tr><td>" + region.getKey().region() + " / " + location.getKey().location() + "</td><td>" + sev + "</td><td>" +
                                severity.getValue().stream().map(l -> l.format(dtf)).collect(Collectors.joining(", ")) + "</td></tr>");
                    }
                    builder.append("<tr>");
                    builder.append("</tr>");
                }
            }
            builder.append("</table>");
            activeAlertSummary = builder.toString();
        }

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<title>Forecasts</title>\n" +
                "<style>\n" +
                EmailCssFileReader.getStyle() + "\n" +
                "</style></head><body>\n" +
                activeAlertSummary + "\n";
    }

    @Override
    public String formatForecastTableHeader(String tableHeader) {
        return String.format("\n<div class=\"forecastTableContainer\">\n<span class=\"forecastTableHeader\">%s</span>\n", tableHeader);
    }

    public String formatForecastTable(List<? extends AggregateForecast> forecasts) {
        return formatForecastTable(forecasts, null);
    }

    public String formatForecastTable(List<? extends AggregateForecast> forecasts, Map<LocalDate, List<String>> activeAlerts) {
        StringBuilder html = new StringBuilder("<table>\n");

        Map<LocalDate, List<? extends ForecastMetric>> dataByDate = forecasts.stream().collect(Collectors.toMap(AggregateForecast::getDate, AggregateForecast::getMetrics));
        List<LocalDate> dateHeaders = dataByDate.keySet().stream().sorted(LocalDate::compareTo).toList();
        Set<ForecastMetricType> metricsSet = new HashSet<>();
        forecasts.forEach(f -> f.getMetrics().forEach(m -> metricsSet.add(m.getType())));

        List<ForecastMetricType> orderedMetrics = metricsSet.stream().sorted().toList();

        html.append("<tr><th class=\"maxWidth\">Metric</th><th class=\"maxWidth\">");
        html.append(dateHeaders.stream().map(LocalDate::toString).collect(Collectors.joining("</th><th class=\"maxWidth\">")));
        html.append("</th></tr>\n");

        for (ForecastMetricType orderedMetric : orderedMetrics) {
            html.append(String.format("<tr><td>%s</td>", WordUtils.capitalize(orderedMetric.toString().replace("_", " ").toLowerCase())));

            for (LocalDate date : dateHeaders) {
                boolean foundMetric = false;
                if (activeAlerts != null && activeAlerts.containsKey(date)) {
                    html.append(String.format("<td class=\"%s\">", String.join(" ", activeAlerts.get(date))));
                } else {
                    html.append("<td class=\"noAlert\">");
                }
                List<? extends ForecastMetric> metrics = dataByDate.get(date);

                for (ForecastMetric<?> metric : metrics) {
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
        return String.format("\n<span class=\"forecastTableFooter\">%s</span>\n</div>\n<br/>\n", tableFooter);
    }

    @Override
    public String formatReportFooter() {
        return "</body></html>";
    }
}
