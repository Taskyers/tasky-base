package pl.taskyers.taskybase.dashboard.main.slo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.dashboard.main.dto.ProjectDTO;
import pl.taskyers.taskybase.project.dao.ProjectDAO;

import java.util.List;

@Service
@AllArgsConstructor
public class MainDashboardSLOImpl implements MainDashboardSLO {
    
    private final ProjectDAO projectDAO;
    
    @Override
    public List<ProjectDTO> getProjects(int n) {
        return projectDAO.getProjects(n);
    }
    
}
