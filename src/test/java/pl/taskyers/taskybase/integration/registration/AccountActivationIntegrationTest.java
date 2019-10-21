package pl.taskyers.taskybase.integration.registration;

import org.junit.Test;
import org.springframework.http.MediaType;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.integration.IntegrationBase;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.CoreMatchers.is;

public class AccountActivationIntegrationTest extends IntegrationBase {
    
    @Test
    public void givenNotExistingTokenWhenActivatingAccountShouldReturnStatus404() throws Exception {
        String token = "test";
        mockMvc.perform(get("/activateAccount/" + token))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Token was not found: " + token)))
                .andExpect(jsonPath("$.type", is(MessageType.WARN.toString())))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void givenSameTokenTwoTimesWhenActivatingAccountShouldFirstReturnStatus200ThenShouldReturnStatus404() throws Exception {
        String token = "tested-token";
        mockMvc.perform(get("/activateAccount/" + token))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Account has been activated")))
                .andExpect(jsonPath("$.type", is(MessageType.SUCCESS.toString())))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/activateAccount/" + token))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Token was not found: " + token)))
                .andExpect(jsonPath("$.type", is(MessageType.WARN.toString())))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isNotFound());
    }
    
}
