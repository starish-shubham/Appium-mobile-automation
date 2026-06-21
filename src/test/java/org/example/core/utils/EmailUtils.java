package org.example.core.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.testng.ITestContext;

import java.io.File;
import java.util.Properties;

public class EmailUtils {

    public static void sendEmailWithReport() {
        // 1. Setup SMTP Server Properties (Example using Gmail)
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // 2. Sender Credentials
        final String senderEmail = "shubham.9315sgt@gmail.com";
//        final String appPassword = "qtzc mrmk cziy xssl"; // Your generated App Password
        final String appPassword = "qtzc"; // Your generated App Password

        // 3. Recipient Email
        final String recipientEmail = "shubhamk0108@gmail.com";

        // 4. Create Session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPassword);
            }
        });

        try {
            // 5. Compose the email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Automation Test Execution Report");

            // 6. Create Email Text Body
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("Hello Team,\n\nThe mobile automation test suite execution has completed. Please find the detailed Extent Report attached.\n\nBest Regards,\nShubham Gupta");

            // 7. Attach the Extent Report HTML file
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            String reportPath = "target/ExtentReports/ExtentReport.html";
            File reportFile = new File(reportPath);

            if (reportFile.exists()) {
                System.out.println("Report file found atxx: " + reportPath);
                attachmentBodyPart.attachFile(reportFile);
            } else {
                System.out.println("Report file not found at: " + reportPath);
                return;
            }

            // 8. Combine text and attachment
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

            // 9. Send the Email
            System.out.println("Sending email with Extent Report attachment...");
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (Exception e) {
            System.err.println("Failed to send execution email report.");
            e.printStackTrace();
        }
    }
}