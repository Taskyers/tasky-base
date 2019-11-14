package pl.taskyers.taskybase.integration.project;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

public class SettingsEntryPointIntegrationTest extends IntegrationBase {
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectNameToEntryPointShouldReturnStatus404() throws Exception {
        final String name = "notExistingName";
        mockMvc.perform(get("/secure/projects/settings/" + name))
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
    @WithMockUser(value = "enabled")
    public void givenUserWithoutRoleToEditAndDeleteToEntryPointShouldReturnStatus403() throws Exception {
        mockMvc.perform(get("/secure/projects/settings/test1"))
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
    @WithMockUser(value = "userWithNoPermissionToUpdateProject")
    @Transactional
    public void givenUserWithoutRoleToEditButWithRoleToDeleteToEntryPointShouldReturnStatus200() throws Exception {
        final String name = "test1";
        ProjectEntity projectEntity = projectRepository.findByName(name).get();
        mockMvc.perform(get("/secure/projects/settings/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(Math.toIntExact(projectEntity.getId()))))
                .andExpect(jsonPath("$.name", is(projectEntity.getName())))
                .andExpect(jsonPath("$.description", is(projectEntity.getDescription())))
                .andExpect(jsonPath("$.canEditProject", is(false)))
                .andExpect(jsonPath("$.canDeleteProject", is(true)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = "userWithNoPermissionToDeleteProject")
    @Transactional
    public void givenUserWithRoleToEditButWithoutRoleToDeleteToEntryPointShouldReturnStatus200() throws Exception {
        final String name = "test1";
        ProjectEntity projectEntity = projectRepository.findByName(name).get();
        mockMvc.perform(get("/secure/projects/settings/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(Math.toIntExact(projectEntity.getId()))))
                .andExpect(jsonPath("$.name", is(projectEntity.getName())))
                .andExpect(jsonPath("$.description", is(projectEntity.getDescription())))
                .andExpect(jsonPath("$.canEditProject", is(true)))
                .andExpect(jsonPath("$.canDeleteProject", is(false)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenUserWithRoleToEditAndRoleToDeleteToEntryPointShouldReturnStatus200() throws Exception {
        final String name = "test1";
        ProjectEntity projectEntity = projectRepository.findByName(name).get();
        mockMvc.perform(get("/secure/projects/settings/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(Math.toIntExact(projectEntity.getId()))))
                .andExpect(jsonPath("$.name", is(projectEntity.getName())))
                .andExpect(jsonPath("$.description", is(projectEntity.getDescription())))
                .andExpect(jsonPath("$.canEditProject", is(true)))
                .andExpect(jsonPath("$.canDeleteProject", is(true)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
}
