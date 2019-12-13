package pl.taskyers.taskybase.integration.dashboard.project;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.core.roles.constants.Roles;
import pl.taskyers.taskybase.core.roles.entity.RoleLinkerEntity;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProjectDashboardIntegrationTest extends IntegrationBase {
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectNameWhenGettingProjectDashboardShouldReturnStatus404() throws Exception {
        final String name = "asdfg";
        
        mockMvc.perform(get("/secure/dashboard/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project with name " + name + " was not found")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenUserNotInProjectWhenGettingProjectDashboardShouldReturnStatus403() throws Exception {
        mockMvc.perform(get("/secure/dashboard/test1"))
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
    public void givenProjectOwnerWhenGettingProjectDashboardShouldReturnStatus200() throws Exception {
        final UserEntity userEntity = userRepository.findByUsername(DEFAULT_USERNAME).get();
        final ProjectEntity projectEntity = projectRepository.findById(1L).get();
        final int tasksSize = taskRepository.findAllByAssigneeAndProject(userEntity, projectEntity).size();
        final RoleLinkerEntity roleLinkerEditProject = roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_EDIT_PROJECT).get();
        final RoleLinkerEntity roleLinkerManageUsers = roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_MANAGE_USERS).get();
        final RoleLinkerEntity roleLinkerInvite = roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.PROJECT_INVITE_OTHERS).get();
        final RoleLinkerEntity roleLinkerManageStatuses = roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_MANAGE_STATUSES).get();
        final RoleLinkerEntity roleLinkerManageTypes = roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_MANAGE_TYPES).get();
        final RoleLinkerEntity roleLinkerManagePriorities = roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_MANAGE_PRIORITIES).get();
        final RoleLinkerEntity roleLinkerManageSprints = roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_MANAGE_SPRINTS).get();
        
        mockMvc.perform(get("/secure/dashboard/" + projectEntity.getName()))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(8)))
                .andExpect(jsonPath("$.tasks.length()", is(tasksSize)))
                .andExpect(jsonPath("$.tasks[0].length()", is(5)))
                .andExpect(jsonPath("$.tasks[0].key").exists())
                .andExpect(jsonPath("$.tasks[0].name").exists())
                .andExpect(jsonPath("$.tasks[0].type").exists())
                .andExpect(jsonPath("$.tasks[0].priority").exists())
                .andExpect(jsonPath("$.tasks[0].status").exists())
                .andExpect(jsonPath("$.manageUsers", is(roleLinkerManageUsers.isChecked())))
                .andExpect(jsonPath("$.editProject", is(roleLinkerEditProject.isChecked())))
                .andExpect(jsonPath("$.invite", is(roleLinkerInvite.isChecked())))
                .andExpect(jsonPath("$.manageStatuses", is(roleLinkerManageStatuses.isChecked())))
                .andExpect(jsonPath("$.manageTypes", is(roleLinkerManageTypes.isChecked())))
                .andExpect(jsonPath("$.managePriorities", is(roleLinkerManagePriorities.isChecked())))
                .andExpect(jsonPath("$.manageSprints", is(roleLinkerManageSprints.isChecked())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = "userWith4Projects")
    @Transactional
    public void givenProjectUserWhenGettingProjectDashboardShouldReturnStatus200() throws Exception {
        final UserEntity userEntity = userRepository.findByUsername("userWith4Projects").get();
        final ProjectEntity projectEntity = projectRepository.findById(1L).get();
        final int tasksSize = taskRepository.findAllByAssigneeAndProject(userEntity, projectEntity).size();
        final boolean roleLinkerEditProject = roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_EDIT_PROJECT).isPresent() && roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_EDIT_PROJECT).get().isChecked();
        final boolean roleLinkerManageUsers = roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_MANAGE_USERS).isPresent() && roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_MANAGE_USERS).get().isChecked();
        final boolean roleLinkerInvite = roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.PROJECT_INVITE_OTHERS).isPresent() && roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.PROJECT_INVITE_OTHERS).get().isChecked();
        final boolean roleLinkerManageStatuses = roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_MANAGE_STATUSES).isPresent() && roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_MANAGE_STATUSES).get().isChecked();
        final boolean roleLinkerManageTypes = roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_MANAGE_TYPES).isPresent() && roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_MANAGE_TYPES).get().isChecked();
        final boolean roleLinkerManagePriorities = roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_MANAGE_PRIORITIES).isPresent() && roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_MANAGE_PRIORITIES).get().isChecked();
        final boolean roleLinkerManageSprints = roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_MANAGE_SPRINTS).isPresent() && roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity,
                Roles.SETTINGS_MANAGE_SPRINTS).get().isChecked();
        
        mockMvc.perform(get("/secure/dashboard/" + projectEntity.getName()))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(8)))
                .andExpect(jsonPath("$.tasks.length()", is(tasksSize)))
                .andExpect(jsonPath("$.tasks[0].key").doesNotExist())
                .andExpect(jsonPath("$.tasks[0].name").doesNotExist())
                .andExpect(jsonPath("$.tasks[0].type").doesNotExist())
                .andExpect(jsonPath("$.tasks[0].priority").doesNotExist())
                .andExpect(jsonPath("$.tasks[0].status").doesNotExist())
                .andExpect(jsonPath("$.manageUsers", is(roleLinkerManageUsers)))
                .andExpect(jsonPath("$.editProject", is(roleLinkerEditProject)))
                .andExpect(jsonPath("$.invite", is(roleLinkerInvite)))
                .andExpect(jsonPath("$.manageStatuses", is(roleLinkerManageStatuses)))
                .andExpect(jsonPath("$.manageTypes", is(roleLinkerManageTypes)))
                .andExpect(jsonPath("$.managePriorities", is(roleLinkerManagePriorities)))
                .andExpect(jsonPath("$.manageSprints", is(roleLinkerManageSprints)))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
}
