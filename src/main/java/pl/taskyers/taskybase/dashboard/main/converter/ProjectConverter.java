package pl.taskyers.taskybase.dashboard.main.converter;

import pl.taskyers.taskybase.dashboard.main.dto.ProjectDTO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

public class ProjectConverter {
    
    private ProjectConverter() {
    }
    
    public static ProjectDTO convertToDTO(ProjectEntity projectEntity) {
        String personals = projectEntity.getOwner()
                                   .getName() + " " + projectEntity.getOwner()
                                   .getSurname();
        return new ProjectDTO(projectEntity.getName(), projectEntity.getDescription(), personals);
    }
    
}
