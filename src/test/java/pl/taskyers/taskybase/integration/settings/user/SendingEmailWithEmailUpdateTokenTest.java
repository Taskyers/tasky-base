package pl.taskyers.taskybase.integration.settings.user;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.integration.IntegrationBase;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SendingEmailWithEmailUpdateTokenTest extends IntegrationBase {
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenInvalidEmailFormatWhenRequestingTokenShouldReturnStatus400() throws Exception {
        String email = "test";
        mockMvc.perform(post("/secure/settings/user/email").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("email", email))
                .andDo(print())
                .andExpect(jsonPath("$[0].message", is("Provided email is invalid")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("email")))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenEmptyEmailDataWhenRequestingTokenShouldReturnStatus400() throws Exception {
        String email = "";
        mockMvc.perform(post("/secure/settings/user/email").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("email", email))
                .andDo(print())
                .andExpect(jsonPath("$[0].message", is("Email cannot be empty")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("email")))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenExistingEmailWhenRequestingTokenShouldReturnStatus400() throws Exception {
        String email = "u1@email.com";
        mockMvc.perform(post("/secure/settings/user/email").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("email", email))
                .andDo(print())
                .andExpect(jsonPath("$[0].message", is("User with " + email + " email already exists")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("email")))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenExistingEmailWhenRequestingTokenShouldReturnStatus200() throws Exception {
        String email = "new@email.com";
        mockMvc.perform(post("/secure/settings/user/email").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("email", email))
                .andDo(print())
                .andExpect(jsonPath("$.message", is("Email with token was sent to provided address: " + email)))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
}
