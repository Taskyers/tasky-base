package pl.taskyers.taskybase.dashboard.main.converter;

import pl.taskyers.taskybase.dashboard.main.dto.ProjectDTO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

public class ProjectConverter {
    
    public static ProjectEntity convertFromDTO(ProjectDTO projectDTO) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setDescription(projectDTO.getDescription());
        projectEntity.setName(projectDTO.getName());
        return projectEntity;
    }
    
    public static ProjectDTO convertToDTO(ProjectEntity projectEntity) {
        String personals = projectEntity.getOwner().getName() + " " + projectEntity.getOwner().getSurname();
        return new ProjectDTO(projectEntity.getName(), projectEntity.getDescription(), personals);
    }
    
}
