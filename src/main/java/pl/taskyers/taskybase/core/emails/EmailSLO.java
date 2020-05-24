package pl.taskyers.taskybase.core.emails;

import pl.taskyers.taskybase.core.users.dto.AccountDTO;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import java.util.List;

/**
 * Interface for sending emails
 *
 * @author Jakub Sildatk
 */
public interface EmailSLO {
    
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
    boolean sendEmailWithTemplateToSingleAddressee(AccountDTO accountDTO, String subject,
            String templatePath, String[] keys, Object[] values);
    
    /**
     * Sending email to list of addresses
     *
     * @param emails     list of addressDTO
     * @param personals  personals of user who has made changes
     * @param taskEntity updated task
     * @since 0.0.7
     */
    void sendEmailToWatchers(List<AddressDTO> emails, String personals, TaskEntity taskEntity);
    
}
