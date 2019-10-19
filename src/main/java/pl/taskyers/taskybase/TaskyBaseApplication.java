package pl.taskyers.taskybase;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEmailTools
public class TaskyBaseApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TaskyBaseApplication.class, args);
    }
    
}
