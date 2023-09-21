package org.kcrha.weather.notifications;

import lombok.Getter;
import org.kcrha.weather.EmailCssFileReader;
import org.kcrha.weather.models.forecast.AggregateForecast;
import org.kcrha.weather.models.forecast.metrics.ForecastMetric;
import org.kcrha.weather.models.forecast.metrics.ForecastMetricType;
import org.kcrha.weather.models.forecast.metrics.HeatRiskIndex;
import org.kcrha.weather.models.rules.RuleSet;
import org.kcrha.weather.models.rules.parser.RulesFileReader;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class HtmlAlertNotificationFormatter implements NotificationFormatter {

    public String formatHeader() {
        return "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                EmailCssFileReader.getStyle() + "\n" +
                "</style>\n" +
                "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
                "</head>\n";
    }

    @Override
    public String formatForecastTableHeader(String tableHeader) {
        return String.format("\n<span class=\"forecastTableHeader\">%s</span>\n", tableHeader);
    }

    public String formatForecastTable(List<? extends AggregateForecast> forecasts) {
        StringBuilder html = new StringBuilder("<table>\n");

        List<? extends ForecastMetric> orderedMetrics = null;
        forecasts.sort(Comparator.comparing(AggregateForecast::getDate));

        List<RuleSet> ruleSets = RulesFileReader.getRules();
        Map<LocalDate, List<String>> daysWithActiveAlerts = RulesFileReader.getDatesWithActiveAlerts(ruleSets, forecasts);

        for (AggregateForecast forecast : forecasts) {
            if (orderedMetrics == null) {
                orderedMetrics = forecast.getMetrics();
                html.append("<tr><th>Date</th><th>");
                html.append(orderedMetrics.stream().map(ForecastMetric::getLongName).collect(Collectors.joining("</th><th>")));
                html.append("</th></tr>\n");
            }
            html.append("<tr");
            if (daysWithActiveAlerts.containsKey(forecast.getDate())) {
                html.append(String.format(" class=\"%s\" ", String.join(" ", daysWithActiveAlerts.get(forecast.getDate()))));
            }
            html.append("><td>");
            html.append(forecast.getDate());
            html.append("</td>");
            Map<ForecastMetricType, ForecastMetric> metrics = forecast.getMetrics().stream().collect(Collectors.toMap(ForecastMetric::getType, e -> e));
            html.append(orderedMetrics.stream().map(orderedMetric -> {
                String tableData = "<td>";
                if (metrics.get(orderedMetric.getType()) == null) {
                    tableData += "N/A</td>";
                } else if (metrics.get(orderedMetric.getType()) != null
                        && orderedMetric.getType().equals(ForecastMetricType.HEAT_RISK_INDEX)
                        && metrics.get(orderedMetric.getType()) instanceof HeatRiskIndex) {
                    return formatHeatRiskIndex((HeatRiskIndex) metrics.get(orderedMetric.getType()));
                } else {
                    tableData += metrics.get(orderedMetric.getType()).getValue();
                    tableData += "</td>";
                }
                return tableData;
            }).collect(Collectors.joining("")));
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
        return "</html>";
    }

    private String formatHeatRiskIndex(HeatRiskIndex index) {
        if (index == null || index.value == null) {
            return "<td>N/A</td>";
        }
        if (HeatRiskIndex.INDEX_COLORS.containsKey(index.value)) {
            return String.format("<td class=\"heatRisk-%s\">%s (%s)</td>", HeatRiskIndex.INDEX_COLORS.get(index.value), index.value, HeatRiskIndex.INDEX_COLORS.get(index.value));
        } else {
            return String.format("<td>N/A (Unexpected Value: %s)</td>", index.value);
        }
    }
}
