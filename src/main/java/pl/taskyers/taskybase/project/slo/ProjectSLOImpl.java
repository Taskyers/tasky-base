package pl.taskyers.taskybase.project.slo;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.roles.slo.RoleSLO;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.dashboard.main.converter.ProjectConverter;
import pl.taskyers.taskybase.dashboard.main.dto.ProjectDTO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.project.repository.ProjectRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class ProjectSLOImpl implements ProjectSLO {
    
    private final AuthProvider authProvider;
    
    private final ProjectRepository projectRepository;
    
    private final RoleSLO roleSLO;
    
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
        roleSLO.createAllRolesForOwner(userEntity, projectEntity);
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
        roleSLO.createAllRolesForUser(loggedUser, projectEntity);
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
    
    private List<ProjectDTO> getProjectsAsDTO(List<ProjectEntity> projectEntities) {
        List<ProjectDTO> result = new ArrayList<>();
        for ( ProjectEntity projectEntity : projectEntities ) {
            result.add(ProjectConverter.convertToDTO(projectEntity));
        }
        return result;
    }
    
}