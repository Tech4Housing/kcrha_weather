package org.kcrha.weather.notifications;

import lombok.Getter;
import org.kcrha.weather.models.cli.PropertyReader;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;

@Getter
public class EmailNotification implements Notification {

    private static final String MAIL_HOST_SERVER_PROPERTY = "MAIL_HOST_SERVER";
    private static final String MAIL_HOST_PORT_PROPERTY = "MAIL_HOST_PORT";
    private static final String MAIL_HOST_USER_PROPERTY = "MAIL_HOST_USER";
    private static final String MAIL_HOST_PASS_PROPERTY = "MAIL_HOST_PASS";
    private static final String MAIL_TO_NAME_PROPERTY = "MAIL_TO_NAME";
    private static final String MAIL_TO_EMAIL_PROPERTY = "MAIL_TO_EMAIL";
    private static final String MAIL_FROM_NAME_PROPERTY = "MAIL_FROM_NAME";
    private static final String MAIL_FROM_EMAIL_PROPERTY = "MAIL_FROM_EMAIL";

    @Override
    public boolean send(String message) {
        try {
            sendEmail(message);
        } catch (RuntimeException e) {
            System.out.println("Failed to send email alert!");
            return false;
        }
        return true;
    }

    private void sendEmail(String htmlMessage) {
        Email email = prepareEmail(htmlMessage);
        Mailer mailer = prepareMailer();

        mailer.sendMail(email);
    }

    private Email prepareEmail(String htmlMessage) {
        try {
            Properties secretProperties = PropertyReader.getSecretProperties();

            return EmailBuilder.startingBlank()
                    .to((String) secretProperties.get(MAIL_TO_NAME_PROPERTY), (String) secretProperties.get(MAIL_TO_EMAIL_PROPERTY))
                    .from((String) secretProperties.get(MAIL_FROM_NAME_PROPERTY), (String) secretProperties.get(MAIL_FROM_EMAIL_PROPERTY))
                    .withSubject(String.format("[AUTOMATED ALERT] Weather Alert -- Date: %s", LocalDate.now()))
                    .withHTMLText(htmlMessage)
                    .buildEmail();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Mailer prepareMailer() {
        try {
            Properties secretProperties = PropertyReader.getSecretProperties();

            return MailerBuilder
                .withSMTPServer(
                        (String) secretProperties.get(MAIL_HOST_SERVER_PROPERTY),
                        (Integer) secretProperties.get(MAIL_HOST_PORT_PROPERTY),
                        (String) secretProperties.get(MAIL_HOST_USER_PROPERTY),
                        (String) secretProperties.get(MAIL_HOST_PASS_PROPERTY))
                .withSessionTimeout(10 * 1000)
                .clearEmailValidator()
                .buildMailer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
