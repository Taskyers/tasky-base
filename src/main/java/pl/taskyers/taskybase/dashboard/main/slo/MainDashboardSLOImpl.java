package pl.taskyers.taskybase.dashboard.main.slo;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.project.slo.ProjectSLO;

@Service
@AllArgsConstructor
public class MainDashboardSLOImpl implements MainDashboardSLO {
    
    private final ProjectSLO projectSLO;
    
    @Override
    public ResponseEntity getProjects(int n) {
        return ResponseEntity.ok(projectSLO.getProjects(n));
    }
    
}
