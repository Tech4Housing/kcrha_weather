package org.kcrha.weather.notifications;

import lombok.Getter;
import org.kcrha.weather.EmailCssFileReader;
import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.AggregateForecast;
import org.kcrha.weather.models.forecast.metrics.ForecastMetric;
import org.kcrha.weather.models.forecast.metrics.ForecastMetricType;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class ForecastHtmlReportFormatter implements ReportFormatter {
    public String formatReportHeader() {
        return formatReportHeader(new HashMap<>());
    }

    public String formatReportHeader(Map<Region, Map<Location, Map<LocalDate, List<String>>>> allActiveAlerts) {
        return "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                EmailCssFileReader.getStyle() + "\n" +
                "</style>\n" +
                "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
                "</head>\n" +
                "<body>\n\n";
    }

    @Override
    public String formatForecastTableHeader(String tableHeader) {
        return String.format("\n<span class=\"forecastTableHeader\">%s</span>\n", tableHeader);
    }

    public String formatForecastTable(List<? extends AggregateForecast> forecasts) {
        return formatForecastTable(forecasts, null);
    }

    public String formatForecastTable(List<? extends AggregateForecast> forecasts, Map<LocalDate, List<String>> activeAlerts) {
        StringBuilder html = new StringBuilder("<table>\n");

        List<? extends ForecastMetric> orderedMetrics = null;
        forecasts.sort(Comparator.comparing(AggregateForecast::getDate));

        for (AggregateForecast forecast : forecasts) {
            if (orderedMetrics == null) {
                orderedMetrics = forecast.getMetrics();
                html.append("<tr><th>Date</th><th>");
                html.append(orderedMetrics.stream().map(ForecastMetric::getLongName).collect(Collectors.joining("</th><th>")));
                html.append("</th></tr>\n");
            }
            html.append("<tr><td>");
            html.append(forecast.getDate());
            html.append("</td>");
            Map<ForecastMetricType, ForecastMetric> metrics = forecast.getMetrics().stream().collect(Collectors.toMap(ForecastMetric::getType, e -> e));
            html.append(orderedMetrics.stream().map(orderedMetric -> {
                String tableData = "<td>";
                if (metrics.get(orderedMetric.getType()) == null) {
                    tableData += "N/A</td>";
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
    public String formatReportFooter() {
        return "</body></html>";
    }
}
