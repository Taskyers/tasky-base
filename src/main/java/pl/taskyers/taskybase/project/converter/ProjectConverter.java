package pl.taskyers.taskybase.project.converter;

import pl.taskyers.taskybase.project.dto.ProjectDTO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

public class ProjectConverter {
    
    public static ProjectEntity convertFromDTO(ProjectDTO projectDTO) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setDescription(projectDTO.getDescription());
        projectEntity.setName(projectDTO.getName());
        return projectEntity;
    }
    
    public static ProjectDTO convertToDTO(ProjectEntity projectEntity) {
        return new ProjectDTO(projectEntity.getName(), projectEntity.getDescription());
    }
    
}
