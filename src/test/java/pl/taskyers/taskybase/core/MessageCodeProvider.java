package pl.taskyers.taskybase.core;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import pl.taskyers.taskybase.core.messages.MessageCode;

public class MessageCodeProvider {
    
    public static void setMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        MessageCode.setMessageSource(messageSource);
    }
    
}
