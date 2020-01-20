package pl.taskyers.taskybase.integration.settings.user;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.settings.user.dto.PasswordDTO;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UpdatingUserPasswordTest extends IntegrationBase {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenInvalidNewPasswordFormatUpdatingUserSettingsPasswordShouldReturnStatus400() throws Exception {
        String invalidPassword = "invalid";
        String oldPassword = "admin";
        String projectJSON = objectMapper.writeValueAsString(new PasswordDTO(oldPassword, invalidPassword));
        mockMvc.perform(patch("/secure/settings/user/password").contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message", is("Provided password is not strong enough")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("newPassword")))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenInvalidCurrentPasswordUpdatingUserSettingsPasswordShouldReturnStatus400() throws Exception {
        String invalidPassword = "invalid";
        String oldPassword = "invalid";
        String projectJSON = objectMapper.writeValueAsString(new PasswordDTO(oldPassword, invalidPassword));
        mockMvc.perform(patch("/secure/settings/user/password").contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message", is("Current password is invalid")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("currentPassword")))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    @Transactional
    public void givenValidPasswordUpdatingUserSettingsPasswordShouldReturnStatus200() throws Exception {
        String validPassword = "Admin12#";
        String oldPassword = "admin";
        String projectJSON = objectMapper.writeValueAsString(new PasswordDTO(oldPassword, validPassword));
        mockMvc.perform(patch("/secure/settings/user/password").contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Password was successfully updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
        
        UserEntity userEntity = userRepository.findByUsername("enabled").get();
        assertFalse(passwordEncoder.matches(oldPassword, userEntity.getPassword()));
        assertTrue(passwordEncoder.matches(validPassword, userEntity.getPassword()));
        
    }
    
    @Test
    @WithMockUser(value = "enabled")
    @Transactional
    public void givenRepeatedNewPasswordUpdatingUserSettingsPasswordShouldReturnStatus400() throws Exception {
        
        String validPassword = "Admin12#";
        String oldPassword = "admin";
        String projectJSON = objectMapper.writeValueAsString(new PasswordDTO(oldPassword, validPassword));
        mockMvc.perform(patch("/secure/settings/user/password").contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Password was successfully updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
        
        UserEntity userEntity = userRepository.findByUsername("enabled").get();
        assertFalse(passwordEncoder.matches(oldPassword, userEntity.getPassword()));
        assertTrue(passwordEncoder.matches(validPassword, userEntity.getPassword()));
        
        oldPassword = "Admin12#";
        projectJSON = objectMapper.writeValueAsString(new PasswordDTO(oldPassword, oldPassword));
        mockMvc.perform(patch("/secure/settings/user/password").contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message", is("New password should be different than current one")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("newPassword")))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isBadRequest());
        
        assertTrue(passwordEncoder.matches(validPassword, userEntity.getPassword()));
        
    }
    
}
