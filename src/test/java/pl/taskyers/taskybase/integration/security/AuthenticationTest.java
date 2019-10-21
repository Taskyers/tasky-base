package pl.taskyers.taskybase.integration.security;

import org.junit.Test;
import org.springframework.http.MediaType;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.integration.security.dto.UserDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

public class AuthenticationTest extends IntegrationBase {
    
    @Test
    public void givenInvalidUsernameAndPasswordShouldReturnStatus401() throws Exception {
        UserDTO userDTO = new UserDTO("dupa", "blada");
        String user = objectMapper.writeValueAsString(userDTO);
        
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(user))
                .andDo(print())
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void givenInvalidPasswordShouldReturnStatus401() throws Exception {
        UserDTO userDTO = new UserDTO("enabled", "blada");
        String user = objectMapper.writeValueAsString(userDTO);
        
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(user))
                .andDo(print())
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void givenInvalidUsernameShouldReturnStatus401() throws Exception {
        UserDTO userDTO = new UserDTO("dupa", "admin");
        String user = objectMapper.writeValueAsString(userDTO);
        
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(user))
                .andDo(print())
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void givenValidUsernameAndPasswordShouldReturnStatus200() throws Exception {
        UserDTO userDTO = new UserDTO("enabled", "admin");
        String user = objectMapper.writeValueAsString(userDTO);
        
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(user))
                .andDo(print())
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    public void givenValidUsernameAndPasswordButNotEnabledShouldReturnStatus401() throws Exception {
        UserDTO userDTO = new UserDTO("notEnabled", "admin");
        String user = objectMapper.writeValueAsString(userDTO);
        
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(user))
                .andDo(print())
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void givenLogoutUrlShouldReturnStatus200() throws Exception {
        
        mockMvc.perform(get("/logout"))
                .andDo(print())
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
}
