package pl.taskyers.taskybase.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.taskyers.taskybase.core.dto.AccountDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application.properties")
@AutoConfigureMockMvc
public abstract class IntegrationBase {
    
    @Autowired
    protected MockMvc mockMvc;
    
    protected final ObjectMapper objectMapper = new ObjectMapper();
    
    protected final String createJSONAccount(AccountDTO accountDTO) {
        StringBuilder json = new StringBuilder("{");
        json.append("\"username\": \"").append(accountDTO.getUsername()).append("\",");
        json.append("\"password\": \"").append(accountDTO.getPassword()).append("\",");
        json.append("\"email\": \"").append(accountDTO.getEmail()).append("\",");
        json.append("\"name\": \"").append(accountDTO.getName()).append("\",");
        json.append("\"surname\": \"").append(accountDTO.getSurname()).append("\"");
        json.append("}");
        return json.toString();
    }
    
}
