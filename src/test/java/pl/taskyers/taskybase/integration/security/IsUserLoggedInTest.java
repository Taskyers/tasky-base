package pl.taskyers.taskybase.integration.security;

import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.integration.IntegrationBase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.CoreMatchers.is;

public class IsUserLoggedInTest extends IntegrationBase {
    
    @Test
    public void givenNotLoggedInUserWhenCheckingIfUserIsLoggedInShouldReturnFalse() throws Exception {
        mockMvc.perform(get("/isLoggedIn"))
                .andDo(print())
                .andExpect(jsonPath("$", is(false)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenLoggedInUserWhenCheckingIfUserIsLoggedInShouldReturnTrue() throws Exception {
        mockMvc.perform(get("/isLoggedIn"))
                .andDo(print())
                .andExpect(jsonPath("$", is(true)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
}
