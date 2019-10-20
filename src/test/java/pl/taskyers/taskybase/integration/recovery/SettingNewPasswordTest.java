package pl.taskyers.taskybase.integration.recovery;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.taskyers.taskybase.core.entity.UserEntity;
import pl.taskyers.taskybase.core.repository.UserRepository;
import pl.taskyers.taskybase.integration.IntegrationBase;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class SettingNewPasswordTest extends IntegrationBase {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Test
    public void givenNotExistingTokenWhenSettingNewPasswordShouldReturnStatus404() throws Exception {
        String token = "test";
        String newPassword = "test";
        UserEntity userEntity = userRepository.findById(1L).get();
        String oldPassword = userEntity.getPassword();
        mockMvc.perform(post("/passwordRecovery/" + token).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("password", newPassword))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.message", is("Token was not found")))
               .andExpect(jsonPath("$.type", is("WARN")))
               .andExpect(jsonPath("$.object", is(nullValue())))
               .andExpect(forwardedUrl(null))
               .andExpect(redirectedUrl(null))
               .andExpect(status().isNotFound());
        
        userEntity = userRepository.findById(1L).get();
        assertEquals(oldPassword, userEntity.getPassword());
    }
    
    @Test
    public void givenExistingTokenWhenSettingNewPasswordShouldReturnStatus200() throws Exception {
        String token = "tested-token";
        String newPassword = "test";
        UserEntity userEntity = userRepository.findById(1L).get();
        String oldPassword = userEntity.getPassword();
        mockMvc.perform(post("/passwordRecovery/" + token).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("password", newPassword))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.message", is("Password successfully changed")))
               .andExpect(jsonPath("$.type", is("SUCCESS")))
               .andExpect(jsonPath("$.object", is(nullValue())))
               .andExpect(forwardedUrl(null))
               .andExpect(redirectedUrl(null))
               .andExpect(status().isOk());
        
        userEntity = userRepository.findById(1L).get();
        assertFalse(passwordEncoder.matches(oldPassword, userEntity.getPassword()));
        assertTrue(passwordEncoder.matches(newPassword, userEntity.getPassword()));
    }
    
}
