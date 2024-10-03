package Smartcontactmanager.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class Emailser {

    private static final String FROM_EMAIL = "shafalibairagi@gmail.com";
    private static final String EMAIL_PASSWORD = "rfip hixt aruk mnxd"; // Consider using environment variables

    public boolean sendEmail(String subject, String message, String to) {
        boolean f = false;
        String host = "smtp.gmail.com";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });

        session.setDebug(true);

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(FROM_EMAIL));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(message,"text/html");

            Transport.send(mimeMessage);
            f = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }
}
