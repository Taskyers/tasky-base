package pl.taskyers.taskybase.sprint.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;
import pl.taskyers.taskybase.sprint.repository.SprintRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class SprintDAOImpl implements SprintDAO {
    
    private final SprintRepository sprintRepository;
    
    @Override
    public SprintEntity addNew(SprintEntity sprintEntity) {
        log.debug("Saving {} sprint in {} project", sprintEntity.getName(), sprintEntity.getProject().getName());
        return sprintRepository.save(sprintEntity);
    }
    
    @Override
    public SprintEntity update(SprintEntity sprintEntity) {
        log.debug("Updating {} sprint", sprintEntity.getName());
        sprintEntity.setStart(DateUtils.addDayToDate(sprintEntity.getStart()));
        sprintEntity.setEnd(DateUtils.addDayToDate(sprintEntity.getEnd()));
        return sprintRepository.save(sprintEntity);
    }
    
    @Override
    public void delete(Long id) {
        log.debug("Deleting sprint with id {}", id);
        sprintRepository.deleteById(id);
    }
    
    @Override
    public boolean doesNameExistsInProject(String name, ProjectEntity project) {
        return sprintRepository.findByNameAndProject(name, project).isPresent();
    }
    
    @Override
    public Optional<SprintEntity> getById(Long id) {
        return sprintRepository.findById(id);
    }
    
    @Override
    public List<SprintEntity> getAllByProject(ProjectEntity projectEntity) {
        return sprintRepository.findAllByProject(projectEntity);
    }
    
    @Override
    public boolean doesSprintInPeriodExists(ProjectEntity project, Date start, Date end) {
        return sprintRepository.findAllByProjectAndStartBeforeAndEndAfter(project, start, end).size() > 0 ||
               sprintRepository.findAllByProjectAndStartGreaterThanEqualAndEndLessThanEqual(project, start, end).size() > 0;
    }
    
    @Override
    public Optional<SprintEntity> getByNameAndProject(String name, ProjectEntity projectEntity) {
        return sprintRepository.findByNameAndProject(name, projectEntity);
    }
    
}
