package pl.taskyers.taskybase.core.emails;

import pl.taskyers.taskybase.core.users.dto.AccountDTO;

/**
 * Interface for sending emails
 *
 * @author Jakub Sildatk
 */
public interface EmailSLO {
    
    /**
     * Sending email to single user using address and personal
     *
     * @param address      email address where message will be sent
     * @param personal     name + surname
     * @param subject      message subject
     * @param templatePath path to email template in ftl
     * @param keys         keys for model map
     * @param values       values for model map
     * @return true if email was sent otherwise false
     * @since 0.0.1
     */
    boolean sendEmailWithTemplateToSingleAddressee(String address, String personal, String subject, String templatePath, String[] keys,
            Object[] values);
    
    /**
     * Sending email to single user using data from accountDTO
     *
     * @param accountDTO   object that contains user account data
     * @param subject      message subject
     * @param templatePath path to email template in ftl
     * @param keys         keys for model map
     * @param values       values for model map
     * @return true if email was sent otherwise false
     * @since 0.0.1
     */
    boolean sendEmailWithTemplateToSingleAddressee(AccountDTO accountDTO, String subject, String templatePath, String[] keys, Object[] values);
    
}
