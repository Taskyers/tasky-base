package pl.taskyers.taskybase.core.emails;

import com.google.common.collect.Lists;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.dto.AccountDTO;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static pl.taskyers.taskybase.core.emails.EmailConstants.*;

@Service
@AllArgsConstructor
@Slf4j
public class EmailSLOImpl implements EmailSLO {
    
    private final EmailService emailService;
    
    @Override
    public void sendEmailWithTemplateToSingleAddressee(AccountDTO accountDTO, String subject, String templatePath, String[] keys, Object[] values) {
        String address = accountDTO.getEmail();
        String personal = accountDTO.getName() + " " + accountDTO.getSurname();
        sendEmail(address, personal, subject, templatePath, createModel(keys, values));
    }
    
    @Override
    public void sendEmailWithTemplateToSingleAddressee(String address, String personal, String subject, String templatePath, String[] keys,
            Object[] values) {
        sendEmail(address, personal, subject, templatePath, createModel(keys, values));
    }
    
    private void sendEmail(String address, String personal, String subject, String templatePath, Map<String, Object> model) {
        try {
            final Email email = DefaultEmail.builder()
                    .from(new InternetAddress(SENDER_ADDRESS, SENDER_PERSONAL))
                    .to(Lists.newArrayList(new InternetAddress(address, personal)))
                    .subject(subject)
                    .body("")
                    .encoding(ENCODING).build();
            emailService.send(email, templatePath, model);
        } catch ( UnsupportedEncodingException | CannotSendEmailException e ) {
            e.printStackTrace();
            log.error("Could not send an email. Address: " + address + ", personals: " + personal + ", subject: " + subject + ", template: " +
                      templatePath);
        }
    }
    
    private Map<String, Object> createModel(String[] keys, Object[] values) {
        Map<String, Object> model = new HashMap<>();
        for ( int i = 0; i < keys.length; i++ ) {
            model.put(keys[i], values[i]);
        }
        return model;
    }
    
}
