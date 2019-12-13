package pl.taskyers.taskybase.task.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.task.entity.TaskEntity;
import pl.taskyers.taskybase.task.repository.TaskRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TaskDAOImpl implements TaskDAO {
    
    private final TaskRepository taskRepository;
    
    @Override
    public List<TaskEntity> getTasksAssignedToUserInProject(UserEntity userEntity, ProjectEntity projectEntity) {
        return taskRepository.findAllByAssigneeAndProject(userEntity, projectEntity);
    }
    
}
