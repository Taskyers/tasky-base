package pl.taskyers.taskybase.project.slo;

import pl.taskyers.taskybase.dashboard.main.dto.ProjectDTO;

import java.util.Set;

/**
 * Interface for operations on project entity
 *
 * @author Jakub Sildatk
 */
public interface ProjectSLO {
    
    /**
     * Get n currently logged in user's projects as DTO converted from ProjectEntity
     *
     * @param n number of projects to be returned
     * @return n projects as DTO
     * @since 0.0.3
     */
    Set<ProjectDTO> getProjects(int n);
    
}
