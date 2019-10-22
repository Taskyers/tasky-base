package pl.taskyers.taskybase.core.emails;

import pl.taskyers.taskybase.core.users.dto.AccountDTO;

public interface EmailSLO {
    
    boolean sendEmailWithTemplateToSingleAddressee(String address, String personal, String subject, String templatePath, String[] keys, Object[] values);
    
    boolean sendEmailWithTemplateToSingleAddressee(AccountDTO accountDTO, String subject, String templatePath, String[] keys, Object[] values);
    
}
