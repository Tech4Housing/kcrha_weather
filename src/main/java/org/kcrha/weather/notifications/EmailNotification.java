package org.kcrha.weather.notifications;

import lombok.Getter;
import org.apache.commons.mail.EmailException;
import org.kcrha.weather.EmailCssFileReader;
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
    public boolean send(String message) {
        try {
            sendEmail(message);
        } catch (EmailException e) {
            System.out.println("Failed to send email alert!");
            return false;
        }
        return true;
    }

    private void sendEmail(String message) throws EmailException {
        // Create the email message
//        HtmlEmail email = new HtmlEmail();
//        email.setHostName("mail.myserver.com");
//        email.addTo("jdoe@somewhere.org", "John Doe");
//        email.setFrom("me@apache.org", "Me");
//        email.setSubject("Test email with HTML");
//
//        // set the html message
//        email.setHtmlMsg("<html>Test</html>");

        System.out.print(message);

        // set the alternative message
//        email.setTextMsg("Your email client does not support HTML messages");
//
//        // send the email
//        email.send();
    }
}
