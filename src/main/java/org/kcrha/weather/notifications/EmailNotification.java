package org.kcrha.weather.notifications;

import jakarta.activation.FileDataSource;
import jakarta.mail.Message;
import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.email.Recipient;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.kcrha.weather.models.cli.PropertyReader.getSecretProperty;

@Getter
public class EmailNotification implements Notification {

    private static final String MAIL_HOST_SERVER_PROPERTY = "MAIL_HOST_SERVER";
    private static final String MAIL_HOST_PORT_PROPERTY = "MAIL_HOST_PORT";
    private static final String MAIL_HOST_USER_PROPERTY = "MAIL_HOST_USER";
    private static final String MAIL_HOST_PASS_PROPERTY = "MAIL_HOST_PASS";
    private static final String MAIL_TO_PROPERTY = "MAIL_TO";
    private static final String MAIL_FROM_NAME_PROPERTY = "MAIL_FROM_NAME";
    private static final String MAIL_FROM_EMAIL_PROPERTY = "MAIL_FROM_EMAIL";

    @Override
    public boolean send(String message) {
        try {
            sendEmail(message);
        } catch (RuntimeException | IOException e) {
            System.out.println("Failed to send email alert!");
            System.out.println(e);
            return false;
        }
        return true;
    }

    private void sendEmail(String htmlMessage) throws IOException {

        Document document = Jsoup.parse(htmlMessage, "UTF-8");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        System.out.println(document.html());

        try (OutputStream outputStream = new FileOutputStream("output.pdf")) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            renderer.setDocumentFromString(document.html());
            renderer.layout();
            renderer.createPDF(outputStream);
        }

        Email email = prepareEmail(htmlMessage);
        Mailer mailer = prepareMailer();

        mailer.sendMail(email);
    }

    private Email prepareEmail(String fileName) {
        try {
            String recipientsString = getSecretProperty(MAIL_TO_PROPERTY);
            List<String> recipientParts = Arrays.stream(recipientsString.split(";")).toList();
            List<Recipient> recipients = recipientParts.stream().map(r -> {
                String[] recipientPart = r.split(",");
                return new Recipient(recipientPart[0], recipientPart[1], Message.RecipientType.TO);
            }).toList();

            return EmailBuilder.startingBlank()
                    .withRecipients(recipients)
                    .from(getSecretProperty(MAIL_FROM_NAME_PROPERTY), getSecretProperty(MAIL_FROM_EMAIL_PROPERTY))
                    .withSubject(String.format("[AUTOMATED ALERT] Weather Alert -- Date: %s", LocalDate.now()))
                    .withHTMLText("<html><body><h1>Forecasts</h1></body></html>")
                    .withAttachment("forecasts", new FileDataSource("output.pdf"))
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
