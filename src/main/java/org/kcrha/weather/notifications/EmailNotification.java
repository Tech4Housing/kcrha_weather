package org.kcrha.weather.notifications;

import lombok.Getter;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import java.io.IOException;
import java.time.LocalDate;

import static org.kcrha.weather.models.cli.PropertyReader.getSecretProperty;

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
            System.out.println(e);
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
            return EmailBuilder.startingBlank()
                    .to(getSecretProperty(MAIL_TO_NAME_PROPERTY), getSecretProperty(MAIL_TO_EMAIL_PROPERTY))
                    .from(getSecretProperty(MAIL_FROM_NAME_PROPERTY), getSecretProperty(MAIL_FROM_EMAIL_PROPERTY))
                    .withSubject(String.format("[AUTOMATED ALERT] Weather Alert -- Date: %s", LocalDate.now()))
                    .withHTMLText(htmlMessage)
                    .buildEmail();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Mailer prepareMailer() {
        try {
            return MailerBuilder
                    .withSMTPServer(
                            getSecretProperty(MAIL_HOST_SERVER_PROPERTY),
                            Integer.valueOf(getSecretProperty(MAIL_HOST_PORT_PROPERTY)),
                            getSecretProperty(MAIL_HOST_USER_PROPERTY),
                            getSecretProperty(MAIL_HOST_PASS_PROPERTY)
                    )
                    .withTransportStrategy(TransportStrategy.SMTP_TLS)
                    .trustingAllHosts(true)
                    .withSessionTimeout(10 * 1000)
                    .clearEmailValidator()
                    .buildMailer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
