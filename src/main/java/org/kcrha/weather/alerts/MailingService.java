package org.kcrha.weather.alerts;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.kcrha.weather.models.AggregateForecast;

import java.util.List;

public class MailingService implements AlertService {


    @Override
    public boolean sendAlert(List<AggregateForecast> forecasts) {
        try {
            sendEmail(forecasts);
        } catch (EmailException e) {
            System.out.println("Failed to send email alert!");
            return false;
        }
        return true;
    }

    private void sendEmail(List<AggregateForecast> forecasts) throws EmailException {
        // Create the email message
        HtmlEmail email = new HtmlEmail();
        email.setHostName("mail.myserver.com");
        email.addTo("jdoe@somewhere.org", "John Doe");
        email.setFrom("me@apache.org", "Me");
        email.setSubject("Test email with HTML");

        // set the html message
        email.setHtmlMsg("<html>Test</html>");

        // set the alternative message
        email.setTextMsg("Your email client does not support HTML messages");

        // send the email
        email.send();
    }
}
