package pl.taskyers.taskybase.integration.project;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.entry.dto.CustomizableEntryDTO;
import pl.taskyers.taskybase.entry.entity.StatusEntryEntity;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class StatusEntriesIntegrationTest extends IntegrationBase {
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectNameToEntryPointShouldReturnStatus404() throws Exception {
        final String name = "notExistingProject";
        
        mockMvc.perform(get("/secure/projects/settings/statuses/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project with name " + name + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenUserWithoutProperRoleToEntryPointShouldReturnStatus403() throws Exception {
        mockMvc.perform(get("/secure/projects/settings/statuses/test1"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have no permission for requested operation")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenUserWithProperRoleToEntryPointShouldReturnStatus200WithData() throws Exception {
        final String name = "test1";
        final int entriesSize = statusEntryRepository.findAllByProject(projectRepository.findByName(name).get()).size();
        
        mockMvc.perform(get("/secure/projects/settings/statuses/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(entriesSize)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].value").exists())
                .andExpect(jsonPath("$[0].textColor").exists())
                .andExpect(jsonPath("$[0].backgroundColor").exists())
                .andExpect(jsonPath("$[0].project").doesNotExist())
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectNameWhenAddingNewEntryShouldReturnStatus404() throws Exception {
        final String name = "notExistingProject";
        final String content = objectMapper.writeValueAsString(new CustomizableEntryDTO());
        
        mockMvc.perform(post("/secure/projects/settings/statuses/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project with name " + name + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenUserWithoutPermissionWhenAddingNewEntryShouldReturnStatus403() throws Exception {
        final String name = "test1";
        final String content = objectMapper.writeValueAsString(new CustomizableEntryDTO());
        
        mockMvc.perform(post("/secure/projects/settings/statuses/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have no permission for requested operation")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenEntryWithDuplicatedValueInProjectWhenAddingNewEntryShouldReturnStatus400() throws Exception {
        final CustomizableEntryDTO customizableEntryDTO = createDTO("test1", "a", "b");
        final String name = "test1";
        final String content = objectMapper.writeValueAsString(customizableEntryDTO);
        final int entriesSizeBefore = statusEntryRepository.findAllByProject(projectRepository.findByName(name).get()).size();
        
        mockMvc.perform(post("/secure/projects/settings/statuses/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].message",
                        is("Entry of type status with " + customizableEntryDTO.getValue() + " value already exists in " + name + " project")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("value")))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        final int entriesSizeAfter = statusEntryRepository.findAllByProject(projectRepository.findByName(name).get()).size();
        
        assertEquals(entriesSizeAfter, entriesSizeBefore);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenEntryWithEmptyValueAndEmptyTextColorAndEmptyBackgroundColorWhenAddingNewEntryShouldReturnStatus400() throws Exception {
        final CustomizableEntryDTO customizableEntryDTO = createDTO("", "", "");
        final String name = "test1";
        final String content = objectMapper.writeValueAsString(customizableEntryDTO);
        final int entriesSizeBefore = statusEntryRepository.findAllByProject(projectRepository.findByName(name).get()).size();
        
        mockMvc.perform(post("/secure/projects/settings/statuses/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$[0].message", is("Status value cannot be empty")))
                .andExpect(jsonPath("$[1].message", is("Text color cannot be empty")))
                .andExpect(jsonPath("$[2].message", is("Background color cannot be empty")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[1].type", is("ERROR")))
                .andExpect(jsonPath("$[2].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("value")))
                .andExpect(jsonPath("$[1].field", is("textColor")))
                .andExpect(jsonPath("$[2].field", is("backgroundColor")))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        final int entriesSizeAfter = statusEntryRepository.findAllByProject(projectRepository.findByName(name).get()).size();
        
        assertEquals(entriesSizeAfter, entriesSizeBefore);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenValidEntryWhenAddingNewEntryShouldReturnStatus201() throws Exception {
        final CustomizableEntryDTO customizableEntryDTO = createDTO("newEntry", "a", "a");
        final String name = "test1";
        final String content = objectMapper.writeValueAsString(customizableEntryDTO);
        final int entriesSizeBefore = statusEntryRepository.findAllByProject(projectRepository.findByName(name).get()).size();
        
        mockMvc.perform(post("/secure/projects/settings/statuses/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(jsonPath("$.message", is("Status entry has been created")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object").exists())
                .andExpect(redirectedUrlPattern("**/statuses/" + name + "/{id}"))
                .andExpect(forwardedUrl(null))
                .andExpect(header().exists("Location"))
                .andExpect(status().isCreated());
        
        final int entriesSizeAfter = statusEntryRepository.findAllByProject(projectRepository.findByName(name).get()).size();
        
        assertNotEquals(entriesSizeAfter, entriesSizeBefore);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingEntryIdWhenUpdatingEntryShouldReturnStatus404() throws Exception {
        final long id = 343L;
        final String content = objectMapper.writeValueAsString(new CustomizableEntryDTO());
        
        mockMvc.perform(put("/secure/projects/settings/statuses/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Entry of type status with id " + id + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenUserWithoutRoleWhenUpdatingEntryShouldReturnStatus403() throws Exception {
        final long id = 1L;
        final String content = objectMapper.writeValueAsString(new CustomizableEntryDTO());
        
        mockMvc.perform(put("/secure/projects/settings/statuses/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have no permission for requested operation")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenEntryWithDuplicatedValueInProjectWhenUpdatingEntryShouldReturnStatus400() throws Exception {
        final long id = 2L;
        final CustomizableEntryDTO dto = createDTO("test1", "a", "b");
        final StatusEntryEntity statusEntryEntityBefore = statusEntryRepository.findById(id).get();
        final String content = objectMapper.writeValueAsString(dto);
        
        mockMvc.perform(put("/secure/projects/settings/statuses/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].message",
                        is("Entry of type status with " + dto.getValue() + " value already exists in " +
                           statusEntryEntityBefore.getProject().getName() +
                           " project")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("value")))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        final StatusEntryEntity statusEntryEntityAfter = statusEntryRepository.findById(id).get();
        
        assertEquals(statusEntryEntityBefore, statusEntryEntityAfter);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenEntryWithAllEmptyFieldsWhenUpdatingEntryShouldReturnStatus400() throws Exception {
        final long id = 2L;
        final StatusEntryEntity statusEntryEntityBefore = statusEntryRepository.findById(id).get();
        final String content = objectMapper.writeValueAsString(createDTO("", "", ""));
        
        mockMvc.perform(put("/secure/projects/settings/statuses/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$[0].message", is("Status value cannot be empty")))
                .andExpect(jsonPath("$[1].message", is("Text color cannot be empty")))
                .andExpect(jsonPath("$[2].message", is("Background color cannot be empty")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[1].type", is("ERROR")))
                .andExpect(jsonPath("$[2].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("value")))
                .andExpect(jsonPath("$[1].field", is("textColor")))
                .andExpect(jsonPath("$[2].field", is("backgroundColor")))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        final StatusEntryEntity statusEntryEntityAfter = statusEntryRepository.findById(id).get();
        
        assertEquals(statusEntryEntityBefore, statusEntryEntityAfter);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenValidEntryWhenUpdatingEntryShouldReturnStatus200() throws Exception {
        final Long id = 4L;
        final CustomizableEntryDTO dto = createDTO("new value", "a", "b");
        final String content = objectMapper.writeValueAsString(dto);
        
        final StatusEntryEntity statusEntryEntityBefore = statusEntryRepository.findById(id).get();
        final Long idBefore = statusEntryEntityBefore.getId();
        final String valueBefore = statusEntryEntityBefore.getValue();
        final String textColorBefore = statusEntryEntityBefore.getTextColor();
        final String backgroundColorBefore = statusEntryEntityBefore.getBackgroundColor();
        final ProjectEntity projectEntityBefore = statusEntryEntityBefore.getProject();
        
        mockMvc.perform(put("/secure/projects/settings/statuses/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Entry with id " + id + " has been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object").exists())
                .andExpect(jsonPath("$.object.id", is(Math.toIntExact(id))))
                .andExpect(jsonPath("$.object.value", is(dto.getValue())))
                .andExpect(jsonPath("$.object.textColor", is(dto.getTextColor())))
                .andExpect(jsonPath("$.object.backgroundColor", is(dto.getBackgroundColor())))
                .andExpect(jsonPath("$.object.project").exists())
                .andExpect(jsonPath("$.object.project.id", is(Math.toIntExact(projectEntityBefore.getId()))))
                .andExpect(jsonPath("$.object.project.owner.id", is(Math.toIntExact(projectEntityBefore.getOwner().getId()))))
                .andExpect(jsonPath("$.object.project.users.length()", is(projectEntityBefore.getUsers().size())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final StatusEntryEntity statusEntryEntityAfter = statusEntryRepository.findById(id).get();
        
        assertEquals(idBefore, statusEntryEntityAfter.getId());
        assertEquals(projectEntityBefore, statusEntryEntityAfter.getProject());
        assertNotEquals(valueBefore, statusEntryEntityAfter.getValue());
        assertNotEquals(textColorBefore, statusEntryEntityAfter.getTextColor());
        assertNotEquals(backgroundColorBefore, statusEntryEntityAfter.getBackgroundColor());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenValidEntryWithOnlyChangedValueWhenUpdatingEntryShouldReturnStatus200() throws Exception {
        final Long id = 5L;
        final StatusEntryEntity statusEntryEntityBefore = statusEntryRepository.findById(id).get();
        final CustomizableEntryDTO dto =
                createDTO("new value1", statusEntryEntityBefore.getTextColor(), statusEntryEntityBefore.getBackgroundColor());
        final String content = objectMapper.writeValueAsString(dto);
        
        final Long idBefore = statusEntryEntityBefore.getId();
        final String valueBefore = statusEntryEntityBefore.getValue();
        final String textColorBefore = statusEntryEntityBefore.getTextColor();
        final String backgroundColorBefore = statusEntryEntityBefore.getBackgroundColor();
        final ProjectEntity projectEntityBefore = statusEntryEntityBefore.getProject();
        
        mockMvc.perform(put("/secure/projects/settings/statuses/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Entry with id " + id + " has been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object").exists())
                .andExpect(jsonPath("$.object.id", is(Math.toIntExact(id))))
                .andExpect(jsonPath("$.object.value", is(dto.getValue())))
                .andExpect(jsonPath("$.object.textColor", is(statusEntryEntityBefore.getTextColor())))
                .andExpect(jsonPath("$.object.backgroundColor", is(statusEntryEntityBefore.getBackgroundColor())))
                .andExpect(jsonPath("$.object.project").exists())
                .andExpect(jsonPath("$.object.project.id", is(Math.toIntExact(projectEntityBefore.getId()))))
                .andExpect(jsonPath("$.object.project.owner.id", is(Math.toIntExact(projectEntityBefore.getOwner().getId()))))
                .andExpect(jsonPath("$.object.project.users.length()", is(projectEntityBefore.getUsers().size())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final StatusEntryEntity statusEntryEntityAfter = statusEntryRepository.findById(id).get();
        
        assertEquals(idBefore, statusEntryEntityAfter.getId());
        assertEquals(projectEntityBefore, statusEntryEntityAfter.getProject());
        assertEquals(textColorBefore, statusEntryEntityAfter.getTextColor());
        assertEquals(backgroundColorBefore, statusEntryEntityAfter.getBackgroundColor());
        assertNotEquals(valueBefore, statusEntryEntityAfter.getValue());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingEntryIdWhenDeletingEntryShouldReturnStatus404() throws Exception {
        final long id = 343L;
        
        mockMvc.perform(delete("/secure/projects/settings/statuses/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Entry of type status with id " + id + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenUserWithoutRoleWhenDeletingEntryShouldReturnStatus403() throws Exception {
        final long id = 1L;
        
        mockMvc.perform(delete("/secure/projects/settings/statuses/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have no permission for requested operation")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenExistingEntryIdWhenDeletingEntryShouldReturnStatus200() throws Exception {
        final Long id = 6L;
        final StatusEntryEntity statusEntryEntityBefore = statusEntryRepository.findById(id).get();
        final Long projectId = statusEntryEntityBefore.getProject().getId();
        final int entriesBefore = statusEntryRepository.findAllByProject(projectRepository.findById(projectId).get()).size();
        
        mockMvc.perform(delete("/secure/projects/settings/statuses/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Status entry has been deleted")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final int entriesAfter = statusEntryRepository.findAllByProject(projectRepository.findById(projectId).get()).size();
        final ProjectEntity projectEntityAfter = projectRepository.findById(projectId).get();
        
        assertFalse(statusEntryRepository.findById(id).isPresent());
        assertTrue(projectRepository.findById(projectId).isPresent());
        assertNotEquals(entriesBefore, entriesAfter);
        assertEquals(entriesBefore, entriesAfter + 1);
        assertEquals(statusEntryRepository.findAllByProject(projectRepository.findById(projectId).get()).size(),
                projectEntityAfter.getStatuses().size());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectNameWhenCheckingForValueInProjectShouldReturnStatus404() throws Exception {
        final String name = "notExistingProject";
        
        mockMvc.perform(get("/secure/projects/settings/statuses/" + name + "/asdf"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project with name " + name + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingValueInProjectWhenCheckingForValueInProjectShouldReturnFalse() throws Exception {
        final String name = "test1";
        final String value = "notExistingValue";
        
        mockMvc.perform(get("/secure/projects/settings/statuses/" + name + "/" + value))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(false)))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenExistingValueInProjectWhenCheckingForValueInProjectShouldReturnTrue() throws Exception {
        final String name = "test1";
        final String value = "test1";
        
        mockMvc.perform(get("/secure/projects/settings/statuses/" + name + "/" + value))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
}
