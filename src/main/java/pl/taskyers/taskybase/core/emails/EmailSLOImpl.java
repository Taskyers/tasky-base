package pl.taskyers.taskybase.core.emails;

import com.google.common.collect.Lists;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static pl.taskyers.taskybase.core.emails.EmailConstants.*;

@Service
@AllArgsConstructor
public class EmailSLOImpl implements EmailSLO {
    
    private final EmailService emailService;
    
    @Override
    public Map<String, Object> createModel(String[] keys, Object[] values) {
        Map<String, Object> model = new HashMap<>();
        for ( int i = 0; i < keys.length; i++ ) {
            model.put(keys[i], values[i]);
        }
        return model;
    }
    
    @Override
    public void sendEmailWithPlainTextToSingleAddressee(String address, String personal, String subject, String body)
            throws UnsupportedEncodingException {
        final Email email = DefaultEmail.builder()
                .from(new InternetAddress(EMAIL_SENDER_ADDRESS, EMAIL_SENDER_PERSONAL))
                .to(Lists.newArrayList(new InternetAddress(address, personal)))
                .subject(subject)
                .body(body)
                .encoding(EMAIL_ENCODING).build();
        emailService.send(email);
    }
    
    @Override
    public void sendEmailWithTemplateToSingleAddressee(String address, String personal, String subject, String templatePath,
            Map<String, Object> model)
            throws UnsupportedEncodingException, CannotSendEmailException {
        final Email email = DefaultEmail.builder().from(new InternetAddress(EMAIL_SENDER_ADDRESS, EMAIL_SENDER_PERSONAL))
                .to(Lists.newArrayList(new InternetAddress(address, personal)))
                .subject(subject)
                .body("")
                .encoding(EMAIL_ENCODING).build();
        emailService.send(email, templatePath, model);
    }
    
}
