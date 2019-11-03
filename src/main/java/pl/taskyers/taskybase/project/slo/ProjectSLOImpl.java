package pl.taskyers.taskybase.project.slo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.slo.AuthSLO;
import pl.taskyers.taskybase.dashboard.main.converter.ProjectConverter;
import pl.taskyers.taskybase.dashboard.main.dto.ProjectDTO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.*;

@Service
@AllArgsConstructor
public class ProjectSLOImpl implements ProjectSLO {
    
    private final AuthSLO authSLO;
    
    @Override
    public Set<ProjectDTO> getProjects(int n) {
        Set<ProjectEntity> userProjects = authSLO.getUserEntity().getProjects();
        Set<ProjectDTO> result = new HashSet<>();
        for ( ProjectEntity projectEntity : userProjects ) {
            result.add(ProjectConverter.convertToDTO(projectEntity));
            if ( result.size() == n ) {
                break;
            }
        }
        return result;
    }
    
}