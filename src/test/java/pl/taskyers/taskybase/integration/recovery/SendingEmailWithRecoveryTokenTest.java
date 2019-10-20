package pl.taskyers.taskybase.integration.recovery;

import org.junit.Test;
import org.springframework.http.MediaType;
import pl.taskyers.taskybase.integration.IntegrationBase;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.CoreMatchers.is;

public class SendingEmailWithRecoveryTokenTest extends IntegrationBase {
    
    @Test
    public void givenNotExistingEmailWhenRequestingTokenShouldReturnStatus404() throws Exception {
        String email = "test";
        mockMvc.perform(post("/passwordRecovery/requestToken").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("email", email))
                .andDo(print())
                .andExpect(jsonPath("$.message", is("Email was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void givenExistingEmailWhenRequestingTokenShouldReturnStatus200() throws Exception {
        String email = "u1@email.com";
        mockMvc.perform(post("/passwordRecovery/requestToken").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("email", email))
                .andDo(print())
                .andExpect(jsonPath("$.message", is("Email with token was sent to provided address")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    public void givenSameEmailTwoTimesWhenRequestingTokenShouldTwiceReturnStatus200() throws Exception {
        String email = "u1@email.com";
        mockMvc.perform(post("/passwordRecovery/requestToken").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("email", email))
                .andDo(print())
                .andExpect(jsonPath("$.message", is("Email with token was sent to provided address")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
        
        mockMvc.perform(post("/passwordRecovery/requestToken").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("email", email))
                .andDo(print())
                .andExpect(jsonPath("$.message", is("Email with token was sent to provided address")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
}
