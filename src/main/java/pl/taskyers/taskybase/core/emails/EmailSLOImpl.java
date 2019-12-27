package pl.taskyers.taskybase.core.emails;

import com.google.common.collect.Lists;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.users.dto.AccountDTO;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.taskyers.taskybase.core.emails.EmailConstants.*;

@Service
@AllArgsConstructor
@Slf4j
public class EmailSLOImpl implements EmailSLO {
    
    private final EmailService emailService;
    
    @Override
    public boolean sendEmailWithTemplateToSingleAddressee(AccountDTO accountDTO, String subject, String templatePath, String[] keys,
            Object[] values) {
        String address = accountDTO.getEmail();
        String personal = accountDTO.getName() + " " + accountDTO.getSurname();
        return sendEmail(address, personal, subject, templatePath, createModel(keys, values));
    }
    
    @Override
    public void sendEmailToWatchers(List<AddressDTO> watchers, String personals, TaskEntity taskEntity) {
        try {
            final String taskKey = taskEntity.getKey();
            final List<InternetAddress> addresses = convertAddresses(watchers);
            final Email email = DefaultEmail.builder().from(new InternetAddress(SENDER_ADDRESS, SENDER_PERSONAL))
                    .to(addresses)
                    .subject(MessageCode.task_updated.getMessage(taskKey))
                    .body("")
                    .encoding(ENCODING).build();
            emailService.send(email, TASK_UPDATED_PATH,
                    createModel(new String[]{ "taskKey", "taskName", "personals" }, new Object[]{ taskKey, taskEntity.getName(), personals }));
        } catch ( UnsupportedEncodingException | CannotSendEmailException e ) {
            e.printStackTrace();
            log.error("Could not sent an emails");
        }
    }
    
    private boolean sendEmail(String address, String personal, String subject, String templatePath, Map<String, Object> model) {
        try {
            final Email email = DefaultEmail.builder()
                    .from(new InternetAddress(SENDER_ADDRESS, SENDER_PERSONAL))
                    .to(Lists.newArrayList(new InternetAddress(address, personal)))
                    .subject(subject)
                    .body("")
                    .encoding(ENCODING).build();
            emailService.send(email, templatePath, model);
            return true;
        } catch ( UnsupportedEncodingException | CannotSendEmailException e ) {
            e.printStackTrace();
            log.error("Could not send an email. Address: " + address + ", personals: " + personal + ", subject: " + subject + ", template: " +
                      templatePath);
            return false;
        }
    }
    
    private Map<String, Object> createModel(String[] keys, Object[] values) {
        Map<String, Object> model = new HashMap<>();
        for ( int i = 0; i < keys.length; i++ ) {
            model.put(keys[i], values[i]);
        }
        return model;
    }
    
    private List<InternetAddress> convertAddresses(List<AddressDTO> emails) throws UnsupportedEncodingException {
        List<InternetAddress> result = new ArrayList<>();
        for ( AddressDTO addressDTO : emails ) {
            result.add(new InternetAddress(addressDTO.getEmail(), addressDTO.getName() + " " + addressDTO.getSurname()));
        }
        return result;
    }
    
}
