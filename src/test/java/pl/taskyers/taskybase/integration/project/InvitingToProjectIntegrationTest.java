package pl.taskyers.taskybase.integration.project;

import com.google.common.collect.Sets;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.project.entity.ProjectInvitationTokenEntity;
import pl.taskyers.taskybase.project.repository.ProjectInvitationTokenRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.AssertTrue;

import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class InvitingToProjectIntegrationTest extends IntegrationBase {
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenValidUserWithValidRolesWhileInvitingToProjectShouldReturnStatus200() throws Exception {
        String email = "enabled@gmail.com";
        String userName = "enabled";
        String projectName = "test1";
        MultiValueMap<String, String> paraMap = new LinkedMultiValueMap<>();
        paraMap.add("username", userName);
        paraMap.add("projectName", projectName);
        mockMvc.perform(post("/secure/projectInvitation/invitationToken").contentType(MediaType.APPLICATION_FORM_URLENCODED).params(paraMap))
                .andDo(print())
                .andExpect(jsonPath("$.message", is("Email with token was sent to provided address: " + email)))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
        UserEntity userEntity = userRepository.findByUsername(userName).get();
        assertTrue(projectInvitationTokenRepository.findByUser(userEntity).isPresent());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenValidUserTokenWithValidRolesWhileInvitingToProjectShouldReturnStatus200() throws Exception {
        String token = "tested-token";
        UserEntity userEntity = userRepository.findByUsername("u1").get();
        int sizeBefore = projectRepository.findAllByUsersContaining(Sets.newHashSet(userEntity)).size();
        ProjectInvitationTokenEntity projectInvitationTokenEntity = projectInvitationTokenRepository.findByToken(token).get();
        String projectName = projectInvitationTokenEntity.getProject().getName();
        mockMvc.perform(patch("/secure/projectInvitation/" + token).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have been successfully added to " + projectName + "!")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
        assertFalse(projectInvitationTokenRepository.findByToken(token).isPresent());
        int sizeAfter = projectRepository.findAllByUsersContaining(Sets.newHashSet(userEntity)).size();
        assertNotEquals(sizeBefore, sizeAfter);
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenValidUserWithoutValidRolesWhileInvitingToProjectShouldReturnStatus403() throws Exception {
        MultiValueMap<String, String> paraMap = new LinkedMultiValueMap<>();
        paraMap.add("username", "notEnabled");
        paraMap.add("projectName", "test1");
        mockMvc.perform(post("/secure/projectInvitation/invitationToken").contentType(MediaType.APPLICATION_FORM_URLENCODED).params(paraMap))
                .andDo(print())
                .andExpect(jsonPath("$.message", is("You have no permission for requested operation")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isForbidden());
        UserEntity userEntity = userRepository.findByUsername("notEnabled").get();
        assertFalse(projectInvitationTokenRepository.findByUser(userEntity).isPresent());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingTokenWhileInvitingToProjectShouldReturnStatus404() throws Exception {
        String token = "test";
        mockMvc.perform(patch("/secure/projectInvitation/" + token).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Token was not found: " + token)))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenOtherUserTokenWhileInvitingToProjectShouldReturnStatus404() throws Exception {
        String token = "tested-token";
        UserEntity userEntity = userRepository.findByUsername("enabled").get();
        mockMvc.perform(patch("/secure/projectInvitation/" + token).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Token was not found: " + token)))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isNotFound());
        assertTrue(projectInvitationTokenRepository.findByToken(token).isPresent());
        assertEquals(0, projectRepository.findAllByUsersContaining(Sets.newHashSet(userEntity)).size());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenUserWithoutPermissionToInvitationEntryPointShouldReturnStatus403() throws Exception {
        mockMvc.perform(get("/secure/projectInvitation/test1"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have no permission for requested operation")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectNameToEntryPointShouldReturnStatus404() throws Exception {
        final String name = "testdefhbdfghfgdhgfh";
        mockMvc.perform(get("/secure/projectInvitation/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project with name " + name + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenValidUserAndValidProjectNameToEntryPointShouldReturnStatus200() throws Exception {
        mockMvc.perform(get("/secure/projectInvitation/test1"))
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist())
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
}
