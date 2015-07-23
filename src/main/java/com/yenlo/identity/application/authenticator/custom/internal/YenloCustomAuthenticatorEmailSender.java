package com.yenlo.identity.application.authenticator.custom.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by vklevko on 21-7-2015.
 */
public class YenloCustomAuthenticatorEmailSender {

    private static Log log = LogFactory.getLog(YenloCustomAuthenticatorEmailSender.class);

    public static void sendEmail(String user, String authorizationCode) {
        final String username = "400269c3435e55357";
        final String password = "7f3f15b640c645";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "mailtrap.io");
        props.put("mail.smtp.port", "2525");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("notification@yenlo.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("user@mailtrap.io"));
            message.setSubject("Login confirmation code.");
            message.setText("Dear " + user + ","
                    + "\n\n Here's your authentication confirmation code: " + authorizationCode);

            Transport.send(message);

            log.info("Email sending to " + user + " completed.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
