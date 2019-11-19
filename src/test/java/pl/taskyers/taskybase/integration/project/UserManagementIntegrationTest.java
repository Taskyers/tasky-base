package pl.taskyers.taskybase.integration.project;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.core.roles.dto.RoleDTO;
import pl.taskyers.taskybase.core.roles.entity.RoleLinkerEntity;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.project.converter.RoleConverter;
import pl.taskyers.taskybase.project.dto.RolesWrapper;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserManagementIntegrationTest extends IntegrationBase {
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectNameToEntryPointShouldReturnStatus404() throws Exception {
        final String name = "notExistingProject";
        
        mockMvc.perform(get("/secure/project/settings/users/" + name))
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
        mockMvc.perform(get("/secure/project/settings/users/test1"))
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
    public void givenUserWithProperRoleToEntryPointWithUsersShouldReturnStatus200WithData() throws Exception {
        final String name = "test1";
        final int usersSize = projectRepository.findByName(name).get().getUsers().size();
        
        mockMvc.perform(get("/secure/project/settings/users/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(usersSize)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].personals").exists())
                .andExpect(jsonPath("$[0].username").exists())
                .andExpect(jsonPath("$[0].email").exists())
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingUserIdWhenGettingUserRolesShouldReturnStatus404() throws Exception {
        final long id = 123L;
        
        mockMvc.perform(get("/secure/project/settings/users/" + id + "/test1"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id " + id + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectNameWhenGettingUserRolesShouldReturnStatus404() throws Exception {
        final String name = "notExistingProject";
        
        mockMvc.perform(get("/secure/project/settings/users/1/" + name))
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
    public void givenNotExistingUserInProjectWhenGettingUserRolesShouldReturnStatus404() throws Exception {
        final String name = "test1";
        final long id = 1L;
        
        mockMvc.perform(get("/secure/project/settings/users/" + id + "/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id " + id + " in " + name + " project was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenUserWithoutProperRoleWhenGettingUserRolesShouldReturnStatus403() throws Exception {
        mockMvc.perform(get("/secure/project/settings/users/1/test1"))
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
    public void givenUserWithProperRoleToWhenGettingUserRolesShouldReturnStatus200WithData() throws Exception {
        final String name = "test12";
        final long id = 1L;
        final ProjectEntity projectEntity = projectRepository.findByName(name).get();
        final UserEntity userEntity = userRepository.findById(id).get();
        final int linkersSize = roleLinkerRepository.findAllByUserAndProject(userEntity, projectEntity).size();
        
        mockMvc.perform(get("/secure/project/settings/users/" + id + "/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(linkersSize)))
                .andExpect(jsonPath("$[0].key").exists())
                .andExpect(jsonPath("$[0].description").exists())
                .andExpect(jsonPath("$[0].checked").exists())
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingUserIdWhenDeletingUserShouldReturnStatus404() throws Exception {
        final long id = 123L;
        
        mockMvc.perform(delete("/secure/project/settings/users/" + id + "/test1"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id " + id + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectNameWhenDeletingUserShouldReturnStatus404() throws Exception {
        final String name = "notExistingProject";
        
        mockMvc.perform(delete("/secure/project/settings/users/1/" + name))
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
    public void givenNotExistingUserInProjectWhenDeletingUserShouldReturnStatus404() throws Exception {
        final String name = "test1";
        final long id = 1L;
        
        mockMvc.perform(delete("/secure/project/settings/users/" + id + "/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id " + id + " in " + name + " project was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenUserWithoutProperRoleWhenDeletingUserShouldReturnStatus403() throws Exception {
        mockMvc.perform(delete("/secure/project/settings/users/1/test1"))
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
    public void givenUserWith1ProjectWhenDeletingUserShouldFirstReturnStatus200ThenStatus404() throws Exception {
        final String name = "test10";
        final long id = 10L;
        final ProjectEntity projectEntity = projectRepository.findByName(name).get();
        final UserEntity userEntity = userRepository.findById(id).get();
        final int projectUserSizeBefore = projectEntity.getUsers().size();
        final int userProjectSizeBefore = userEntity.getProjects().size();
        
        assertEquals(1, projectUserSizeBefore);
        assertEquals(1, userProjectSizeBefore);
        
        mockMvc.perform(delete("/secure/project/settings/users/" + id + "/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id " + id + " has been removed from " + name)))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final int projectUserSizeAfter = projectEntity.getUsers().size();
        final int userProjectSizeAfter = userEntity.getProjects().size();
        
        assertEquals(0, projectUserSizeAfter);
        assertEquals(0, userProjectSizeAfter);
        
        mockMvc.perform(delete("/secure/project/settings/users/" + id + "/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id " + id + " in " + name + " project was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenUserWithSeveralProjectsWhenDeletingUserShouldFirstReturnStatus200ThenStatus404() throws Exception {
        final String name = "test15";
        final long id = 11L;
        final ProjectEntity projectEntity = projectRepository.findByName(name).get();
        final UserEntity userEntity = userRepository.findById(id).get();
        final int projectUserSizeBefore = projectEntity.getUsers().size();
        final int userProjectSizeBefore = userEntity.getProjects().size();
        
        mockMvc.perform(delete("/secure/project/settings/users/" + id + "/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id " + id + " has been removed from " + name)))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final int projectUserSizeAfter = projectEntity.getUsers().size();
        final int userProjectSizeAfter = userEntity.getProjects().size();
        
        assertNotEquals(0, projectUserSizeAfter);
        assertNotEquals(0, userProjectSizeAfter);
        assertEquals(projectUserSizeBefore - 1, projectUserSizeAfter);
        assertEquals(userProjectSizeBefore - 1, userProjectSizeAfter);
        
        mockMvc.perform(delete("/secure/project/settings/users/" + id + "/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id " + id + " in " + name + " project was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingUserIdWhenUpdatingUserRolesShouldReturnStatus404() throws Exception {
        final long id = 123L;
        final String content = objectMapper.writeValueAsString(new RolesWrapper());
        
        mockMvc.perform(put("/secure/project/settings/users/" + id + "/test1").contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id " + id + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectNameWhenUpdatingUserRolesShouldReturnStatus404() throws Exception {
        final String name = "notExistingProject";
        final String content = objectMapper.writeValueAsString(new RolesWrapper());
        
        mockMvc.perform(put("/secure/project/settings/users/1/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
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
    public void givenNotExistingUserInProjectWhenUpdatingUserRolesShouldReturnStatus404() throws Exception {
        final String name = "test1";
        final long id = 1L;
        final String content = objectMapper.writeValueAsString(new RolesWrapper());
        
        mockMvc.perform(put("/secure/project/settings/users/" + id + "/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id " + id + " in " + name + " project was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenUserWithoutProperRoleWhenUpdatingUserRolesShouldReturnStatus403() throws Exception {
        final String content = objectMapper.writeValueAsString(new RolesWrapper());
        
        mockMvc.perform(put("/secure/project/settings/users/1/test1").contentType(MediaType.APPLICATION_JSON).content(content))
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
    public void givenUserWithAllRolesCheckedWhenEditingUserWithoutAnyChangesRolesShouldReturnStatus200WithAllRolesChecked() throws Exception {
        final String name = "test1";
        final long id = 12L;
        final UserEntity userEntity = userRepository.findById(id).get();
        final ProjectEntity projectEntity = projectRepository.findByName(name).get();
        final String content = objectMapper.writeValueAsString(new RolesWrapper(prepareRoles(userEntity, projectEntity)));
        final Set<RoleLinkerEntity> linkersBefore = userEntity.getRoleLinkerEntities();
        
        for ( RoleLinkerEntity roleLinkerEntity : linkersBefore ) {
            assertTrue(roleLinkerEntity.isChecked());
        }
        
        mockMvc.perform(put("/secure/project/settings/users/" + id + "/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("All roles have been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object").exists())
                .andExpect(jsonPath("$.object.roles.length()", is(userEntity.getRoleLinkerEntities().size())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final Set<RoleLinkerEntity> linkersAfter = userEntity.getRoleLinkerEntities();
        
        for ( RoleLinkerEntity roleLinkerEntity : linkersAfter ) {
            assertTrue(roleLinkerEntity.isChecked());
        }
        assertEquals(linkersBefore.size(), linkersAfter.size());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenUserWithAllRolesCheckedWhenEditingUserWithChangingAllRolesToNotCheckedRolesShouldReturnStatus200WithAllRolesNotChecked()
            throws Exception {
        final String name = "test1";
        final long id = 13L;
        final UserEntity userEntity = userRepository.findById(id).get();
        final ProjectEntity projectEntity = projectRepository.findByName(name).get();
        final List<RoleDTO> roles = prepareRoles(userEntity, projectEntity);
        for ( RoleDTO roleDTO : roles ) {
            roleDTO.setChecked(false);
        }
        final String content = objectMapper.writeValueAsString(new RolesWrapper(roles));
        final Set<RoleLinkerEntity> linkersBefore = userEntity.getRoleLinkerEntities();
        
        for ( RoleLinkerEntity roleLinkerEntity : linkersBefore ) {
            assertTrue(roleLinkerEntity.isChecked());
        }
        
        mockMvc.perform(put("/secure/project/settings/users/" + id + "/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("All roles have been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object").exists())
                .andExpect(jsonPath("$.object.roles.length()", is(userEntity.getRoleLinkerEntities().size())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final Set<RoleLinkerEntity> linkersAfter = userEntity.getRoleLinkerEntities();
        
        for ( RoleLinkerEntity roleLinkerEntity : linkersAfter ) {
            assertFalse(roleLinkerEntity.isChecked());
        }
        assertEquals(linkersBefore.size(), linkersAfter.size());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenUserWithAllRolesCheckedWhenEditingRolesWithChangingOneRoleShouldReturnStatus200WithOneRoleUnchecked() throws Exception {
        final String name = "test1";
        final long id = 14L;
        final UserEntity userEntity = userRepository.findById(id).get();
        final String roleToBeChanged = "settings.edit.project";
        final ProjectEntity projectEntity = projectRepository.findByName(name).get();
        final List<RoleDTO> roles = prepareRoles(userEntity, projectEntity);
        for ( RoleDTO roleDTO : roles ) {
            if ( roleDTO.getKey().equals(roleToBeChanged) ) {
                roleDTO.setChecked(false);
            }
        }
        final String content = objectMapper.writeValueAsString(new RolesWrapper(roles));
        final Set<RoleLinkerEntity> linkersBefore = userEntity.getRoleLinkerEntities();
        final int checkedBefore = getNumberOfRolesWithValue(linkersBefore, true);
        final int notCheckedBefore = getNumberOfRolesWithValue(linkersBefore, false);
        
        assertNotEquals(checkedBefore, notCheckedBefore);
        assertEquals(0, notCheckedBefore);
        
        mockMvc.perform(put("/secure/project/settings/users/" + id + "/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("All roles have been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object").exists())
                .andExpect(jsonPath("$.object.roles.length()", is(userEntity.getRoleLinkerEntities().size())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final Set<RoleLinkerEntity> linkersAfter = userEntity.getRoleLinkerEntities();
        final int checkedAfter = getNumberOfRolesWithValue(linkersAfter, true);
        final int notCheckedAfter = getNumberOfRolesWithValue(linkersAfter, false);
        
        assertEquals(linkersAfter.size(), linkersBefore.size());
        assertEquals(1, notCheckedAfter);
        assertNotEquals(checkedBefore, checkedAfter);
        assertNotEquals(notCheckedBefore, notCheckedAfter);
        assertNull(isRoleWithValue(linkersAfter, roleToBeChanged, true));
        assertNotNull(isRoleWithValue(linkersAfter, roleToBeChanged, false));
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenUserWithAllRolesNotCheckedWhenEditingRolesWithChangingOneRoleShouldReturnStatus200WithOneRoleChecked() throws Exception {
        final String name = "test1";
        final long id = 15L;
        final UserEntity userEntity = userRepository.findById(id).get();
        final String roleToBeChanged = "project.invite.others";
        final ProjectEntity projectEntity = projectRepository.findByName(name).get();
        final List<RoleDTO> roles = prepareRoles(userEntity, projectEntity);
        for ( RoleDTO roleDTO : roles ) {
            if ( roleDTO.getKey().equals(roleToBeChanged) ) {
                roleDTO.setChecked(true);
            }
        }
        final String content = objectMapper.writeValueAsString(new RolesWrapper(roles));
        final Set<RoleLinkerEntity> linkersBefore = userEntity.getRoleLinkerEntities();
        final int checkedBefore = getNumberOfRolesWithValue(linkersBefore, true);
        final int notCheckedBefore = getNumberOfRolesWithValue(linkersBefore, false);
        
        assertNotEquals(checkedBefore, notCheckedBefore);
        assertEquals(0, checkedBefore);
        
        mockMvc.perform(put("/secure/project/settings/users/" + id + "/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("All roles have been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object").exists())
                .andExpect(jsonPath("$.object.roles.length()", is(userEntity.getRoleLinkerEntities().size())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final Set<RoleLinkerEntity> linkersAfter = userEntity.getRoleLinkerEntities();
        final int checkedAfter = getNumberOfRolesWithValue(linkersAfter, true);
        final int notCheckedAfter = getNumberOfRolesWithValue(linkersAfter, false);
        
        assertEquals(linkersAfter.size(), linkersBefore.size());
        assertEquals(1, checkedAfter);
        assertNotEquals(checkedBefore, checkedAfter);
        assertNotEquals(notCheckedBefore, notCheckedAfter);
        assertNull(isRoleWithValue(linkersAfter, roleToBeChanged, false));
        assertNotNull(isRoleWithValue(linkersAfter, roleToBeChanged, true));
    }
    
    private List<RoleDTO> prepareRoles(UserEntity userEntity, ProjectEntity projectEntity) {
        List<RoleLinkerEntity> roles = roleLinkerRepository.findAllByUserAndProject(userEntity, projectEntity);
        List<RoleDTO> result = new ArrayList<>();
        for ( RoleLinkerEntity roleLinkerEntity : roles ) {
            result.add(RoleConverter.convertFromRoleLinker(roleLinkerEntity));
        }
        return result;
    }
    
    private int getNumberOfRolesWithValue(Set<RoleLinkerEntity> linkers, boolean value) {
        int result = 0;
        for ( RoleLinkerEntity roleLinkerEntity : linkers ) {
            if ( roleLinkerEntity.isChecked() == value ) {
                result++;
            }
        }
        return result;
    }
    
    private RoleLinkerEntity isRoleWithValue(Set<RoleLinkerEntity> roles, String key, boolean value) {
        return roles.stream()
                .filter(roleLinkerEntity -> roleLinkerEntity.getRole().getKey().equals(key) && roleLinkerEntity.isChecked() == value)
                .findFirst()
                .orElse(null);
    }
    
}
