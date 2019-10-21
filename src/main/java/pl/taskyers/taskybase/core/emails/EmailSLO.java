package pl.taskyers.taskybase.core.emails;

import pl.taskyers.taskybase.core.dto.AccountDTO;

public interface EmailSLO {
    
    void sendEmailWithTemplateToSingleAddressee(String address, String personal, String subject, String templatePath, String[] keys, Object[] values);
    
    void sendEmailWithTemplateToSingleAddressee(AccountDTO accountDTO, String subject, String templatePath, String[] keys, Object[] values);
    
}
