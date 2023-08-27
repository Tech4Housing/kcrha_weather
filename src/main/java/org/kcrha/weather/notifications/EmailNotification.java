package org.kcrha.weather.notifications;

import org.apache.commons.mail.EmailException;
import org.kcrha.weather.models.forecast.AggregateForecast;
import org.kcrha.weather.models.forecast.metrics.ForecastMetric;
import org.kcrha.weather.models.forecast.metrics.ForecastMetricType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            Map<ForecastMetricType, String> metrics = forecast.getMetrics().stream().collect(Collectors.toMap(ForecastMetric::getType, ForecastMetric::getValue));
            html.append(orderedMetrics.stream().map(orderedMetric -> metrics.getOrDefault(orderedMetric.getType(), "N/A")).collect(Collectors.joining("</td><td>")));
            html.append("</td></tr>\n");
        }

        html.append("</table>\n");
        return html.toString();
    }
}
