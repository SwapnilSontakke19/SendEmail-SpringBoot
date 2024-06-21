package com.lcwd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmailService {

    public void sendEmail(String message, String subject, String[] to, String from, MultipartFile file) throws IOException {
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("swapnilsontakke7676@gmail.com", "ikbf fytf xdfw ilas");
            }
        });

        session.setDebug(true);

        try {
            MimeMessage m = new MimeMessage(session);
            m.setFrom(from);

            for (String recipient : to) {
                m.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            }

            m.setSubject(subject);

            // Create the message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(message);

            // Create a multipart message
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Part two is the attachment
            if (!file.isEmpty()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(convertToFile(file));
                multipart.addBodyPart(attachmentPart);
            }

            // Send the complete message parts
            m.setContent(multipart);

            // Send message
            Transport.send(m);
            System.out.println("Sent Successfully...!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error sending email", e);
        }
    }

    private File convertToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
}
