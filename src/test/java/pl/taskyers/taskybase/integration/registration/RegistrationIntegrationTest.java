package pl.taskyers.taskybase.integration.registration;

import org.junit.Test;
import org.springframework.http.MediaType;
import pl.taskyers.taskybase.core.entity.UserEntity;
import pl.taskyers.taskybase.integration.IntegrationBase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

public class RegistrationIntegrationTest extends IntegrationBase {
    
    @Test
    public void givenInvalidUserWhenRegisterShouldReturnStatus400() throws Exception {
        String givenUser = objectMapper.writeValueAsString(new UserEntity(null, "", "", "", "", ""));
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(givenUser))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].message", is("Username cannot be empty")))
               .andExpect(jsonPath("$[1].message", is("Email cannot be empty")))
               .andExpect(jsonPath("$[2].message", is("Password cannot be empty")))
               .andExpect(jsonPath("$[3].message", is("Name cannot be empty")))
               .andExpect(jsonPath("$[4].message", is("Surname cannot be empty")))
               .andExpect(forwardedUrl(null))
               .andExpect(redirectedUrl(null))
               .andExpect(status().isBadRequest());
    }
    
    @Test
    public void givenUserWithDuplicatedUsernameWhenRegisterShouldReturnStatus400() throws Exception {
        UserEntity userEntity = new UserEntity(null, "u1", "zaq1@WSX", "Test", "Test", "email@email.com");
        String givenUser = objectMapper.writeValueAsString(userEntity);
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(givenUser))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].message", is("User with " + userEntity.getUsername() + " username already exists")))
               .andExpect(jsonPath("$[0].type", is("ERROR")))
               .andExpect(jsonPath("$[0].field", is("username")))
               .andExpect(forwardedUrl(null))
               .andExpect(redirectedUrl(null))
               .andExpect(status().isBadRequest());
    }
    
    @Test
    public void givenUserWithDuplicatedEmailAndInvalidPasswordWhenRegisterShouldReturnStatus400() throws Exception {
        UserEntity userEntity = new UserEntity(null, "test", "test", "Test", "Test", "u1@email.com");
        String givenUser = objectMapper.writeValueAsString(userEntity);
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(givenUser))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].message", is("User with " + userEntity.getEmail() + " email already exists")))
               .andExpect(jsonPath("$[1].message", is("Provided password is not strong enough")))
               .andExpect(forwardedUrl(null))
               .andExpect(redirectedUrl(null))
               .andExpect(status().isBadRequest());
    }
    
    @Test
    public void givenValidUserWhenRegisterShouldReturnStatus200() throws Exception {
        UserEntity userEntity = new UserEntity(null, "test", "zaq1@WSX", "Test", "Test", "test@email.com");
        String givenUser = objectMapper.writeValueAsString(userEntity);
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(givenUser))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.message", is("Registration was successful")))
               .andExpect(jsonPath("$.type", is("SUCCESS")))
               .andExpect(jsonPath("$.object.id", is(Integer.class)))
               .andExpect(forwardedUrl(null))
               .andExpect(redirectedUrlPattern("**/register/{id}"))
               .andExpect(header().exists("Location"))
               .andExpect(status().isCreated());
    }
    
}
