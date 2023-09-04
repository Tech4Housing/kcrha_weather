package org.kcrha.weather.notifications;

import lombok.Getter;
import org.apache.commons.mail.EmailException;
import org.kcrha.weather.models.forecast.AggregateForecast;
import org.kcrha.weather.models.forecast.metrics.ForecastMetric;
import org.kcrha.weather.models.forecast.metrics.ForecastMetricType;
import org.kcrha.weather.models.forecast.metrics.HeatRiskIndex;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class EmailNotification implements Notification {

    @Override
    public boolean send(List<? extends AggregateForecast> forecasts) {
        try {
            sendEmail(forecasts);
        } catch (EmailException e) {
            System.out.println("Failed to send email alert!");
            return false;
        }
        return true;
    }

    private void sendEmail(List<? extends AggregateForecast> forecasts) throws EmailException {
        // Create the email message
//        HtmlEmail email = new HtmlEmail();
//        email.setHostName("mail.myserver.com");
//        email.addTo("jdoe@somewhere.org", "John Doe");
//        email.setFrom("me@apache.org", "Me");
//        email.setSubject("Test email with HTML");
//
//        // set the html message
//        email.setHtmlMsg("<html>Test</html>");

        System.out.print(prepareHtml(forecasts));

        // set the alternative message
//        email.setTextMsg("Your email client does not support HTML messages");
//
//        // send the email
//        email.send();
    }

    private String prepareHtml(List<? extends AggregateForecast> forecasts) {
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
            html.append("</td><td>");
            Map<ForecastMetricType, ForecastMetric> metrics = forecast.getMetrics().stream().collect(Collectors.toMap(ForecastMetric::getType, e -> e));
            html.append(orderedMetrics.stream().map(orderedMetric -> {
                if (metrics.get(orderedMetric.getType()) == null) {
                    return "N/A";
                }

                if (orderedMetric.getType().equals(ForecastMetricType.HEAT_RISK_INDEX)
                        && metrics.get(orderedMetric.getType()) instanceof HeatRiskIndex) {
                    return ((HeatRiskIndex) metrics.get(orderedMetric.getType())).getHtmlSpan();
                }
                return metrics.get(orderedMetric.getType()).getValue();
            }).collect(Collectors.joining("</td><td>")));
            html.append("</td></tr>\n");
        }

        html.append("</table>\n");
        return html.toString();
    }
}
