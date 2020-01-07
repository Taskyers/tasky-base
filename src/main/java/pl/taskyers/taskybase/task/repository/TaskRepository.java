package pl.taskyers.taskybase.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    
    List<TaskEntity> findAllByAssigneeAndProject(UserEntity assignee, ProjectEntity project);
    
    Optional<TaskEntity> findByNameAndProject(String name, ProjectEntity project);
    
    Optional<TaskEntity> findByKeyAndProject(String key, ProjectEntity project);
    
    Optional<TaskEntity> findByKey(String key);
    
    List<TaskEntity> findAllByProjectAndStatus_ValueAndSprint(ProjectEntity project, String value, SprintEntity sprint);
    
    List<TaskEntity> findTop5ByProjectInAndNameIgnoreCaseContaining(List<ProjectEntity> projectEntities, String name);
    
}