package com.podverbnyj.provider.utils;


import com.podverbnyj.provider.dao.db.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServletRequest;

/**
 * Email sender
 */
public class EmailSender {
    private EmailSender() {
    }

    private static final Logger log = LogManager.getLogger(EmailSender.class);
    private static final String USER_NAME = "testfinalproject2";  // GMail username (just the part before "@gmail.com")
    private static final String PASSWORD = "Test222tesT"; // GMail password



    public static void emailSender(String address, String subject, String body, HttpServletRequest req) throws DBException {
        sendFromGMail(address, subject, body, req);
        log.info("Email sent to address: {}", address);
    }

    /**
     * Gmail file sender
     *
     * @param to recipient email
     * @param subject email subject
     * @param body email body
     * @param req used to take file type to send, or if null ==> email without attachment
     * @throws DBException  high level message for error page.
     */
    private static void sendFromGMail(String to, String subject, String body, HttpServletRequest req) throws DBException {

        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", EmailSender.USER_NAME);
        props.put("mail.smtp.password", EmailSender.PASSWORD);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);

        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(EmailSender.USER_NAME));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            if (req == null) {
                message.setText(body);
            }

            if (req != null) {
                String fileType = req.getParameter("file");
                // Create the message part
                BodyPart messageBodyPart = new MimeBodyPart();

                // Now set the actual message
                messageBodyPart.setText(body);

                // Create a multipart message
                Multipart multipart = new MimeMultipart();

                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                messageBodyPart = new MimeBodyPart();
                String fileName;

                if ("txt".equals(fileType)) {
                    fileName = req.getServletContext().getRealPath("/") + "price_list.txt";
                } else {
                    fileName = req.getServletContext().getRealPath("/") + "price_list.pdf";
                }

                DataSource source = new FileDataSource(fileName);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fileName);
                multipart.addBodyPart(messageBodyPart);

                // Send the complete message parts
                message.setContent(multipart);
            }
            Transport transport = session.getTransport("smtp");
            transport.connect(host, EmailSender.USER_NAME, EmailSender.PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException ae) {
            log.error("Email sending fall down....");
            // chaging message exception to our own exception
            throw new DBException("Email sending error");
        }
    }
}
