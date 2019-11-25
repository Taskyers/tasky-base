package pl.taskyers.taskybase.integration.project;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.entry.dto.CustomizableEntryDTO;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
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
    
    protected CustomizableEntryDTO createDTO(String value, String textColor, String backgroundColor, String type) {
        CustomizableEntryDTO customizableEntryDTO = new CustomizableEntryDTO();
        customizableEntryDTO.setValue(value);
        customizableEntryDTO.setTextColor(textColor);
        customizableEntryDTO.setBackgroundColor(backgroundColor);
        customizableEntryDTO.setEntryType(type);
        return customizableEntryDTO;
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectNameToEntryPointShouldReturnStatus404() throws Exception {
        final String name = "notExistingProject";
        EntryType type = EntryType.TYPE;
        
        mockMvc.perform(get("/secure/projects/settings/entries/" + name + "/" + type))
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
        mockMvc.perform(get("/secure/projects/settings/entries/test1/TYPE"))
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
        EntryType type = EntryType.TYPE;
        final int entriesSize = entryEntityRepository.findAllByProjectAndEntryType(projectRepository.findByName(name).get(), type).size();
        
        mockMvc.perform(get("/secure/projects/settings/entries/" + name + "/" + type))
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
        CustomizableEntryDTO entryDTO = createDTO("tessst", "black", "white", "TYPE");
        final String content = objectMapper.writeValueAsString(entryDTO);
        
        mockMvc.perform(post("/secure/projects/settings/entries/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
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
        CustomizableEntryDTO entryDTO = createDTO("tessst", "black", "white", "TYPE");
        final String content = objectMapper.writeValueAsString(entryDTO);
        
        mockMvc.perform(post("/secure/projects/settings/entries/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
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
        String entryType = "STATUS";
        final CustomizableEntryDTO customizableEntryDTO = createDTO("test1", "a", "b", entryType);
        final String name = "test1";
        final String content = objectMapper.writeValueAsString(customizableEntryDTO);
        final int entriesSizeBefore =
                entryEntityRepository.findAllByProjectAndEntryType(projectRepository.findByName(name).get(), EntryType.valueOf(entryType)).size(); ;
        
        mockMvc.perform(post("/secure/projects/settings/entries/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].message",
                        is("Entry of type " + customizableEntryDTO.getEntryType() + " with " + customizableEntryDTO.getValue() +
                           " value already exists in " + name + " project")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("value")))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        final int entriesSizeAfter =
                entryEntityRepository.findAllByProjectAndEntryType(projectRepository.findByName(name).get(), EntryType.valueOf(entryType)).size();
        
        assertEquals(entriesSizeAfter, entriesSizeBefore);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenEntryWithEmptyValueAndEmptyTextColorAndEmptyBackgroundColorAndEmptyEntryTypeWhenAddingNewEntryShouldReturnStatus404()
            throws Exception {
        final CustomizableEntryDTO customizableEntryDTO = createDTO("", "", "", "");
        final String name = "test1";
        final String content = objectMapper.writeValueAsString(customizableEntryDTO);
        final int entriesSizeBefore = entryEntityRepository.findAll().size();
        
        mockMvc.perform(post("/secure/projects/settings/entries/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Entry of type  was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
        
        final int entriesSizeAfter = entryEntityRepository.findAll().size();
        
        assertEquals(entriesSizeAfter, entriesSizeBefore);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenEntryWithEmptyValueAndEmptyTextColorAndEmptyBackgroundColorWhenAddingNewEntryShouldReturnStatus400() throws Exception {
        String entryType = "TYPE";
        final CustomizableEntryDTO customizableEntryDTO = createDTO("", "", "", entryType);
        final String name = "test1";
        final String content = objectMapper.writeValueAsString(customizableEntryDTO);
        final int entriesSizeBefore =
                entryEntityRepository.findAllByProjectAndEntryType(projectRepository.findByName(name).get(), EntryType.valueOf(entryType)).size();
        
        mockMvc.perform(post("/secure/projects/settings/entries/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$[0].message", is("Entry value cannot be empty")))
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
        
        final int entriesSizeAfter =
                entryEntityRepository.findAllByProjectAndEntryType(projectRepository.findByName(name).get(), EntryType.valueOf(entryType)).size();
        
        assertEquals(entriesSizeAfter, entriesSizeBefore);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenEntryWithEmptyEntryTypeWhenAddingNewEntryShouldReturnStatus404() throws Exception {
        String type = "wrongValue";
        final CustomizableEntryDTO customizableEntryDTO = createDTO("test-test123", "red", "black", type);
        final String name = "test1";
        final String content = objectMapper.writeValueAsString(customizableEntryDTO);
        final int entriesSizeBefore = entryEntityRepository.findAll().size();
        
        mockMvc.perform(post("/secure/projects/settings/entries/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Entry of type " + type + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
        
        final int entriesSizeAfter = entryEntityRepository.findAll().size();
        
        assertEquals(entriesSizeAfter, entriesSizeBefore);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenValidEntryWhenAddingNewEntryShouldReturnStatus201() throws Exception {
        String entryType = "STATUS";
        final CustomizableEntryDTO customizableEntryDTO = createDTO("newEntry", "a", "a", entryType);
        final String name = "test1";
        final String content = objectMapper.writeValueAsString(customizableEntryDTO);
        final int entriesSizeBefore =
                entryEntityRepository.findAllByProjectAndEntryType(projectRepository.findByName(name).get(), EntryType.valueOf(entryType)).size();
        
        mockMvc.perform(post("/secure/projects/settings/entries/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(jsonPath("$.message", is(customizableEntryDTO.getEntryType() + " entry has been created")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object").exists())
                .andExpect(redirectedUrlPattern("**/entries/" + name + "/{id}"))
                .andExpect(forwardedUrl(null))
                .andExpect(header().exists("Location"))
                .andExpect(status().isCreated());
        
        final int entriesSizeAfter =
                entryEntityRepository.findAllByProjectAndEntryType(projectRepository.findByName(name).get(), EntryType.valueOf(entryType)).size();
        
        assertNotEquals(entriesSizeAfter, entriesSizeBefore);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingEntryIdWhenUpdatingEntryShouldReturnStatus404() throws Exception {
        final long id = 343L;
        final String content = objectMapper.writeValueAsString(new CustomizableEntryDTO());
        
        mockMvc.perform(put("/secure/projects/settings/entries/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Entry with id " + id + " was not found")))
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
        
        mockMvc.perform(put("/secure/projects/settings/entries/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
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
    public void givenEntryWithDuplicatedValueAndEntryTypeInProjectWhenUpdatingEntryShouldReturnStatus400() throws Exception {
        final long id = 2L;
        final CustomizableEntryDTO dto = createDTO("test1", "a", "b", "STATUS");
        final EntryEntity entryEntityBefore = entryEntityRepository.findById(id).get();
        final String content = objectMapper.writeValueAsString(dto);
        
        mockMvc.perform(put("/secure/projects/settings/entries/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].message",
                        is("Entry of type " + dto.getEntryType() + " with " + dto.getValue() + " value already exists in " +
                           entryEntityBefore.getProject().getName() + " project")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("value")))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        final EntryEntity entryEntityAfter = entryEntityRepository.findById(id).get();
        
        assertEquals(entryEntityBefore, entryEntityAfter);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenEntryWithAllEmptyFieldsWithValidEntryTypeWhenUpdatingEntryShouldReturnStatus400() throws Exception {
        final long id = 2L;
        final EntryEntity entryEntityBefore = entryEntityRepository.findById(id).get();
        final String content = objectMapper.writeValueAsString(createDTO("", "", "", "STATUS"));
        
        mockMvc.perform(put("/secure/projects/settings/entries/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$[0].message", is("Entry value cannot be empty")))
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
        
        final EntryEntity entryEntityAfter = entryEntityRepository.findById(id).get();
        
        assertEquals(entryEntityBefore, entryEntityAfter);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenValidEntryWhenUpdatingEntryShouldReturnStatus200() throws Exception {
        final Long id = 4L;
        final CustomizableEntryDTO dto = createDTO("new value", "a", "b", "STATUS");
        final String content = objectMapper.writeValueAsString(dto);
        
        final EntryEntity entryEntityBefore = entryEntityRepository.findById(id).get();
        final Long idBefore = entryEntityBefore.getId();
        final String valueBefore = entryEntityBefore.getValue();
        final String textColorBefore = entryEntityBefore.getTextColor();
        final String backgroundColorBefore = entryEntityBefore.getBackgroundColor();
        final ProjectEntity projectEntityBefore = entryEntityBefore.getProject();
        
        mockMvc.perform(put("/secure/projects/settings/entries/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Entry with id " + id + " has been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object").exists())
                .andExpect(jsonPath("$.object.id", is(Math.toIntExact(id))))
                .andExpect(jsonPath("$.object.value", is(dto.getValue())))
                .andExpect(jsonPath("$.object.textColor", is(dto.getTextColor())))
                .andExpect(jsonPath("$.object.backgroundColor", is(dto.getBackgroundColor())))
                .andExpect(jsonPath("$.object.entryType", is(dto.getEntryType())))
                .andExpect(jsonPath("$.object.project").exists())
                .andExpect(jsonPath("$.object.project.id", is(Math.toIntExact(projectEntityBefore.getId()))))
                .andExpect(jsonPath("$.object.project.owner.id", is(Math.toIntExact(projectEntityBefore.getOwner().getId()))))
                .andExpect(jsonPath("$.object.project.users.length()", is(projectEntityBefore.getUsers().size())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final EntryEntity entryEntityAfter = entryEntityRepository.findById(id).get();
        
        assertEquals(idBefore, entryEntityAfter.getId());
        assertEquals(projectEntityBefore, entryEntityAfter.getProject());
        assertNotEquals(valueBefore, entryEntityAfter.getValue());
        assertNotEquals(textColorBefore, entryEntityAfter.getTextColor());
        assertNotEquals(backgroundColorBefore, entryEntityAfter.getBackgroundColor());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenValidEntryWithOnlyChangedValueWhenUpdatingEntryShouldReturnStatus200() throws Exception {
        final Long id = 5L;
        final EntryEntity entryEntityBefore = entryEntityRepository.findById(id).get();
        final CustomizableEntryDTO dto =
                createDTO("new value1", entryEntityBefore.getTextColor(), entryEntityBefore.getBackgroundColor(),
                        entryEntityBefore.getEntryType().name());
        final String content = objectMapper.writeValueAsString(dto);
        
        final Long idBefore = entryEntityBefore.getId();
        final String valueBefore = entryEntityBefore.getValue();
        final String textColorBefore = entryEntityBefore.getTextColor();
        final String backgroundColorBefore = entryEntityBefore.getBackgroundColor();
        final ProjectEntity projectEntityBefore = entryEntityBefore.getProject();
        
        mockMvc.perform(put("/secure/projects/settings/entries/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Entry with id " + id + " has been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object").exists())
                .andExpect(jsonPath("$.object.id", is(Math.toIntExact(id))))
                .andExpect(jsonPath("$.object.value", is(dto.getValue())))
                .andExpect(jsonPath("$.object.textColor", is(entryEntityBefore.getTextColor())))
                .andExpect(jsonPath("$.object.backgroundColor", is(entryEntityBefore.getBackgroundColor())))
                .andExpect(jsonPath("$.object.project").exists())
                .andExpect(jsonPath("$.object.project.id", is(Math.toIntExact(projectEntityBefore.getId()))))
                .andExpect(jsonPath("$.object.project.owner.id", is(Math.toIntExact(projectEntityBefore.getOwner().getId()))))
                .andExpect(jsonPath("$.object.project.users.length()", is(projectEntityBefore.getUsers().size())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final EntryEntity entryEntityAfter = entryEntityRepository.findById(id).get();
        
        assertEquals(idBefore, entryEntityAfter.getId());
        assertEquals(projectEntityBefore, entryEntityAfter.getProject());
        assertEquals(textColorBefore, entryEntityAfter.getTextColor());
        assertEquals(backgroundColorBefore, entryEntityAfter.getBackgroundColor());
        assertNotEquals(valueBefore, entryEntityAfter.getValue());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingEntryIdWhenDeletingEntryShouldReturnStatus404() throws Exception {
        final long id = 343L;
        
        mockMvc.perform(delete("/secure/projects/settings/entries/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Entry with id " + id + " was not found")))
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
        
        mockMvc.perform(delete("/secure/projects/settings/entries/" + id))
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
        final EntryEntity entryEntityBefore = entryEntityRepository.findById(id).get();
        final Long projectId = entryEntityBefore.getProject().getId();
        final int entriesBefore =
                entryEntityRepository.findAllByProjectAndEntryType(projectRepository.findById(projectId).get(), entryEntityBefore.getEntryType())
                        .size();
        
        mockMvc.perform(delete("/secure/projects/settings/entries/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(entryEntityBefore.getEntryType() + " entry has been deleted")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final int entriesAfter =
                entryEntityRepository.findAllByProjectAndEntryType(projectRepository.findById(projectId).get(), entryEntityBefore.getEntryType())
                        .size();
        
        assertFalse(entryEntityRepository.findById(id).isPresent());
        assertTrue(projectRepository.findById(projectId).isPresent());
        assertNotEquals(entriesBefore, entriesAfter);
        assertEquals(entriesBefore, entriesAfter + 1);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectNameWhenCheckingForValueInProjectShouldReturnStatus404() throws Exception {
        final String name = "notExistingProject";
        
        mockMvc.perform(get("/secure/projects/settings/entries/" + name + "/TYPE"))
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
        final String type = "TYPE";
        
        mockMvc.perform(get("/secure/projects/settings/entries/" + name + "/" + value + "/" + type))
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
        final String type = "STATUS";
        
        mockMvc.perform(get("/secure/projects/settings/entries/" + name + "/" + value + "/" + type))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingEntryTypeInProjectWhenCheckingForValueInProjectShouldReturn404() throws Exception {
        final String name = "test1";
        final String value = "test1";
        final String type = "BadType";
        
        mockMvc.perform(get("/secure/projects/settings/entries/" + name + "/" + value + "/" + type))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Entry of type " + type + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
}
