package pl.taskyers.taskybase.integration.settings.user;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.integration.IntegrationBase;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UpdatingUserEmailTest extends IntegrationBase {
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingTokenWhenUpdatingUserEmailShouldReturnStatus404() throws Exception {
        String token = "test";
        String newEmail = "new@email.com";
        UserEntity userEntity = userRepository.findById(1L).get();
        String oldEmail = userEntity.getEmail();
        mockMvc.perform(patch("/secure/settings/user/email/" + token).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("email", newEmail))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Token was not found: " + token)))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isNotFound());
        
        userEntity = userRepository.findById(1L).get();
        assertEquals(oldEmail, userEntity.getEmail());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenExistingTokenWhenUpdatingUserEmailShouldReturnStatus200() throws Exception {
        String token = "tested-token";
        String newEmail = "new@email.com";
        UserEntity userEntity = userRepository.findById(1L).get();
        String oldEmail = userEntity.getEmail();
        mockMvc.perform(patch("/secure/settings/user/email/" + token).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("email", newEmail))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Email was successfully updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
        
        userEntity = userRepository.findById(1L).get();
        assertNotEquals(oldEmail, userEntity.getEmail());
        assertEquals(newEmail, userEntity.getEmail());
    }
    
}
