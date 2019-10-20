package pl.taskyers.taskybase.integration.registration;

import org.junit.Test;
import org.springframework.http.MediaType;
import pl.taskyers.taskybase.core.dto.AccountDTO;
import pl.taskyers.taskybase.integration.IntegrationBase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

public class RegistrationIntegrationTest extends IntegrationBase {
    
    @Test
    public void givenInvalidUserWhenRegisterShouldReturnStatus400() throws Exception {
        AccountDTO accountDTO = new AccountDTO("", "", "", "", "");
        String givenUser = objectMapper.writeValueAsString(accountDTO);
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(givenUser))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].message", is("Username cannot be empty")))
               .andExpect(jsonPath("$[1].message", is("Email cannot be empty")))
               .andExpect(jsonPath("$[2].message", is("Password cannot be empty")))
               .andExpect(jsonPath("$[3].message", is("Name cannot be empty")))
               .andExpect(jsonPath("$[4].message", is("Surname cannot be empty")))
               .andExpect(forwardedUrl(null))
               .andExpect(redirectedUrl(null))
               .andExpect(status().isBadRequest());
    }
    
    @Test
    public void givenUserWithDuplicatedUsernameWhenRegisterShouldReturnStatus400() throws Exception {
        AccountDTO accountDTO = new AccountDTO("u1", "email@email.com", "test", "Test", "Test");
        String givenUser = objectMapper.writeValueAsString(accountDTO);
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(givenUser))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].message", is("User with " + accountDTO.getUsername() + " username already exists")))
               .andExpect(jsonPath("$[0].type", is("ERROR")))
               .andExpect(jsonPath("$[0].field", is("username")))
               .andExpect(forwardedUrl(null))
               .andExpect(redirectedUrl(null))
               .andExpect(status().isBadRequest());
    }
    
    @Test
    public void givenUserWithDuplicatedEmailAndInvalidPasswordWhenRegisterShouldReturnStatus400() throws Exception {
        AccountDTO accountDTO = new AccountDTO("test", "u1@email.com", "test", "Test", "Test");
        String givenUser = objectMapper.writeValueAsString(accountDTO);
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(givenUser))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].message", is("User with " + accountDTO.getEmail() + " email already exists")))
               .andExpect(jsonPath("$[1].message", is("Provided password is not strong enough")))
               .andExpect(forwardedUrl(null))
               .andExpect(redirectedUrl(null))
               .andExpect(status().isBadRequest());
    }
    
    @Test
    public void givenValidUserWhenRegisterShouldReturnStatus200() throws Exception {
        AccountDTO accountDTO = new AccountDTO("test", "test@email.com", "zaq1@WSX", "Test", "Test");
        String givenUser = objectMapper.writeValueAsString(accountDTO);
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(givenUser))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.message", is("Registration was successful. Activation link was sent to email address.")))
               .andExpect(jsonPath("$.type", is("SUCCESS")))
               .andExpect(jsonPath("$.object.id", is(Integer.class)))
               .andExpect(jsonPath("$.length()", is(3)))
               .andExpect(jsonPath("$.object.enabled", is(false)))
               .andExpect(forwardedUrl(null))
               .andExpect(redirectedUrlPattern("**/register/{id}"))
               .andExpect(header().exists("Location"))
               .andExpect(status().isCreated());
    }
    
    @Test
    public void givenNotExistingUsernameWhenCheckingByUsernameShouldReturnFalse() throws Exception {
        String username = "testUsername";
        mockMvc.perform(get("/register/checkByUsername/" + username))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", is(false)))
               .andExpect(forwardedUrl(null))
               .andExpect(redirectedUrl(null))
               .andExpect(status().isOk());
    }
    
    @Test
    public void givenExistingUsernameWhenCheckingByUsernameShouldReturnTrue() throws Exception {
        String username = "u1";
        mockMvc.perform(get("/register/checkByUsername/" + username))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", is(true)))
               .andExpect(forwardedUrl(null))
               .andExpect(redirectedUrl(null))
               .andExpect(status().isOk());
    }
    
    @Test
    public void givenNotExistingEmailWhenCheckingByEmailShouldReturnFalse() throws Exception {
        String email = "test@test.com";
        mockMvc.perform(get("/register/checkByEmail/" + email))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", is(false)))
               .andExpect(forwardedUrl(null))
               .andExpect(redirectedUrl(null))
               .andExpect(status().isOk());
    }
    
    @Test
    public void givenExistingEmailWhenCheckingByEmailShouldReturnTrue() throws Exception {
        String email = "u1@email.com";
        mockMvc.perform(get("/register/checkByEmail/" + email))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", is(true)))
               .andExpect(forwardedUrl(null))
               .andExpect(redirectedUrl(null))
               .andExpect(status().isOk());
    }
    
}
