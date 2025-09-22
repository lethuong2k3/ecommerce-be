package net.fpoly.ecommerce.service.impl.email;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.from.domain}")
    private String fromDomain;

    private final SendGrid sendGrid;

    public EmailService(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public void sendEmail(String to, String subject, String contentHtml) throws IOException {
        Email from = new Email(fromDomain);
        Email recipient = new Email(to);

        // Nội dung HTML
        Content content = new Content("text/html", contentHtml);
        Mail mail = new Mail(from, subject, recipient, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        sendGrid.api(request);
    }
}
