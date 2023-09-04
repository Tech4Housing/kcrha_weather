package org.kcrha.weather.notifications;

import lombok.Getter;
import org.kcrha.weather.EmailCssFileReader;
import org.kcrha.weather.models.cli.Location;
import org.kcrha.weather.models.cli.Region;
import org.kcrha.weather.models.forecast.AggregateForecast;
import org.kcrha.weather.models.forecast.metrics.ForecastMetric;
import org.kcrha.weather.models.forecast.metrics.ForecastMetricType;
import org.kcrha.weather.models.forecast.metrics.HeatRiskIndex;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.kcrha.weather.models.forecast.metrics.HeatRiskIndex.INDEX_COLORS;

@Getter
public class HtmlNotificationFormatter implements NotificationFormatter {

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
    public String formatRegionLocation(Region region, Location location) {
        return String.format("\n<span class=\"regionLocation\">%s (%s)</span><a target=\"_blank\" href=\"https://maps.google.com/?q=%s,%s\"><i class=\"fa fa-external-link\"></i></a>\n", location.location(), region.region(), location.lat(), location.lon());
    }

    public String formatForecasts(List<? extends AggregateForecast> forecasts) {
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
    public String formatFooter() {
        return "</html>";
    }

    private String formatHeatRiskIndex(HeatRiskIndex index) {
        if (index == null || index.value == null) {
            return "<td>N/A</td>";
        }
        if (INDEX_COLORS.containsKey(index.value)) {
            return String.format("<td class=\"heatRisk-%s\">%s (%s)</td>", INDEX_COLORS.get(index.value), index.value, INDEX_COLORS.get(index.value));
        } else {
            return String.format("<td>N/A (Unexpected Value: %s)</td>", index.value);
        }
    }
}
