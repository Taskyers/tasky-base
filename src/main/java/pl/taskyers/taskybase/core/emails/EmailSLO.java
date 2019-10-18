package pl.taskyers.taskybase.core.emails;

import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface EmailSLO {
    
    Map<String, Object> createModel(String[] keys, Object[] values);
    
    void sendEmailWithPlainTextToSingleAddressee(String address, String personal, String subject, String body) throws UnsupportedEncodingException;
    
    void sendEmailWithTemplateToSingleAddressee(String address, String personal, String subject, String templatePath, Map<String, Object> model)
            throws UnsupportedEncodingException, CannotSendEmailException;
    
}
