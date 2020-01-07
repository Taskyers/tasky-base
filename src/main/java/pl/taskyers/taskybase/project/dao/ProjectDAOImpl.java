package pl.taskyers.taskybase.project.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.roles.dao.RoleDAO;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.users.dao.UserDAO;
import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.dashboard.main.converter.ProjectConverter;
import pl.taskyers.taskybase.dashboard.main.dto.ProjectDTO;
import pl.taskyers.taskybase.project.dao.ProjectDAO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.project.repository.ProjectRepository;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectDAOImpl implements ProjectDAO {
    
    private final AuthProvider authProvider;
    
    private final ProjectRepository projectRepository;
    
    private final RoleDAO roleDAO;
    
    private final UserDAO userDAO;
    
    @Override
    public List<ProjectDTO> getProjects(int n) {
        return getProjectsAsDTO(
                projectRepository.findAllByUsersContainingOrderByCreationDateDesc(authProvider.getUserEntityAsSet(), PageRequest.of(0, n)));
    }
    
    @Override
    public List<ProjectDTO> getAllProjects() {
        return getProjectsAsDTO(projectRepository.findAllByUsersContaining(authProvider.getUserEntityAsSet()));
    }
    
    @Override
    public ProjectEntity addNewProject(ProjectEntity projectEntity) {
        final UserEntity userEntity = authProvider.getUserEntity();
        projectEntity.setOwner(userEntity);
        projectEntity.setUsers(new HashSet<UserEntity>() {{
            add(userEntity);
        }});
        projectEntity.setCreationDate(DateUtils.getCurrentTimestamp());
        roleDAO.createAllRolesForOwner(userEntity, projectEntity);
        return projectRepository.save(projectEntity);
    }
    
    @Override
    public Optional<ProjectEntity> getProjectEntityByName(String name) {
        return projectRepository.findByName(name);
    }
    
    @Override
    public ProjectEntity addUserToProject(ProjectEntity projectEntity, UserEntity userEntity) {
        final UserEntity loggedUser = authProvider.getUserEntity();
        if ( loggedUser != userEntity ) {
            return null;
        }
        projectEntity.getUsers().add(loggedUser);
        roleDAO.createAllRolesForUser(loggedUser, projectEntity);
        return projectRepository.save(projectEntity);
    }
    
    @Override
    public Optional<ProjectEntity> getProjectEntityById(Long id) {
        return projectRepository.findById(id);
    }
    
    @Override
    public ProjectEntity updateProject(ProjectEntity projectEntity, String name, String description) {
        projectEntity.setName(name);
        projectEntity.setDescription(description);
        return projectRepository.save(projectEntity);
    }
    
    @Override
    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }
    
    @Override
    public Optional<ProjectEntity> getProjectByNameAndUser(String projectName, UserEntity userEntity) {
        return projectRepository.findByNameAndUsers(projectName, userEntity);
    }
    
    @Override
    public void deleteUserInProject(Long userId, String projectName) {
        log.debug("Try to delete user with id = {} and project with name {}", userId, projectName);
        final ProjectEntity projectEntity = this.getProjectEntityByName(projectName).get();
        final UserEntity userEntity = userDAO.getEntityById(userId).get();
        
        for ( UserEntity user : projectEntity.getUsers() ) {
            if ( user.getId().equals(userId) ) {
                projectEntity.getUsers().remove(user);
                userEntity.getProjects().remove(projectEntity);
                
                projectRepository.flush();
                userDAO.flushRepository();
                
                log.debug("User has been removed");
                break;
            }
        }
    }
    
    @Override
    public Optional<ProjectEntity> getProjectByNameAndOwner(String projectName, UserEntity owner) {
        return projectRepository.findByNameAndOwner(projectName, owner);
    }
    
    @Override
    public List<ProjectEntity> getAllProjectsEntities() {
        return projectRepository.findAllByUsersContaining(authProvider.getUserEntityAsSet());
    }
    
    private List<ProjectDTO> getProjectsAsDTO(List<ProjectEntity> projectEntities) {
        List<ProjectDTO> result = new ArrayList<>();
        for ( ProjectEntity projectEntity : projectEntities ) {
            result.add(ProjectConverter.convertToDTO(projectEntity));
        }
        return result;
    }
    
}