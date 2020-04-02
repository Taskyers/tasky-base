package pl.taskyers.taskybase.project.converter;

import pl.taskyers.taskybase.project.dto.ProjectDTO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

public class ProjectConverter {
    
    private ProjectConverter() {
    }
    
    public static ProjectEntity convertFromDTO(ProjectDTO projectDTO) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setDescription(projectDTO.getDescription());
        projectEntity.setName(projectDTO.getName());
        return projectEntity;
    }
    
}
