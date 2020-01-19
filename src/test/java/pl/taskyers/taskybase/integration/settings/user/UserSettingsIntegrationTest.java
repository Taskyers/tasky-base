package pl.taskyers.taskybase.integration.settings.user;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.settings.user.dto.UserDTO;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class UserSettingsIntegrationTest extends IntegrationBase {
    
    @Test
    public void givenNotLoggedInUserWhenCheckingUserSettingsShouldReturnStatus403() throws Exception {
        mockMvc.perform(get("/secure/settings/user"))
                .andDo(print())
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenLoggedInUserWhenCheckingUserSettingsShouldReturnStatus200() throws Exception {
        UserEntity userEntity = userRepository.findByUsername(DEFAULT_USERNAME).get();
        mockMvc.perform(get("/secure/settings/user"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(userEntity.getName())))
                .andExpect(jsonPath("$.surname", is(userEntity.getSurname())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenValidDataUpdatingUserSettingsShouldReturnStatus200() throws Exception {
        String newName = "Newname";
        String newSurname = "Newsurname";
        
        String projectJSON = objectMapper.writeValueAsString(new UserDTO(newName, newSurname));
        mockMvc.perform(put("/secure/settings/user").contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Account has been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object.name", is(newName)))
                .andExpect(jsonPath("$.object.surname", is(newSurname)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
        UserEntity userEntity = userRepository.findByUsername(DEFAULT_USERNAME).get();
        assertEquals(userEntity.getName(), newName);
        assertEquals(userEntity.getSurname(), newSurname);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenInvalidDataUpdatingUserSettingsShouldReturnStatus400() throws Exception {
        String newName = "";
        String newSurname = "";
        
        String projectJSON = objectMapper.writeValueAsString(new UserDTO(newName, newSurname));
        mockMvc.perform(put("/secure/settings/user").contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message", is("Name cannot be empty")))
                .andExpect(jsonPath("$[1].message", is("Surname cannot be empty")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[1].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(jsonPath("$[1].field", is("surname")))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isBadRequest());
        UserEntity userEntity = userRepository.findByUsername(DEFAULT_USERNAME).get();
        
        assertNotEquals(userEntity.getName(), newName);
        assertNotEquals(userEntity.getSurname(), newSurname);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenInvalidNameAndSurnameFormatUpdatingUserSettingsShouldReturnStatus400() throws Exception {
        String invalidSurname = "invalid";
        String invalidName = "invalid";
        UserEntity userEntity = userRepository.findByUsername(DEFAULT_USERNAME).get();
        String projectJSON = objectMapper.writeValueAsString(new UserDTO(invalidName, invalidSurname));
        mockMvc.perform(put("/secure/settings/user").contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message", is("Provided name is invalid")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(jsonPath("$[1].message", is("Provided surname is invalid")))
                .andExpect(jsonPath("$[1].type", is("ERROR")))
                .andExpect(jsonPath("$[1].field", is("surname")))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isBadRequest());
    }
    
}
