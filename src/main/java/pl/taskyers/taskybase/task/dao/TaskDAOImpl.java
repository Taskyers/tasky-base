package pl.taskyers.taskybase.task.dao;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.entry.dao.EntryDAO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.sprint.dao.SprintDAO;
import pl.taskyers.taskybase.task.entity.TaskEntity;
import pl.taskyers.taskybase.task.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
@Slf4j
public class TaskDAOImpl implements TaskDAO {
    
    private static final String KEY_SEPARATOR = "-";
    
    private static final int KEY_MIN_RANGE = 1;
    
    private static final int KEY_MAX_RANGE = 1000000;
    
    private final TaskRepository taskRepository;
    
    private final EntryDAO entryDAO;
    
    private final SprintDAO sprintDAO;
    
    @Override
    public List<TaskEntity> getTasksAssignedToUserInProject(UserEntity userEntity, ProjectEntity projectEntity) {
        return taskRepository.findAllByAssigneeAndProject(userEntity, projectEntity);
    }
    
    @Override
    public Optional<TaskEntity> getTaskByNameAndProject(String name, ProjectEntity projectEntity) {
        return taskRepository.findByNameAndProject(name, projectEntity);
    }
    
    @Override
    public Optional<TaskEntity> getTaskByKey(String key, ProjectEntity projectEntity) {
        return taskRepository.findByKeyAndProject(key, projectEntity);
    }
    
    @Override
    public TaskEntity createTask(TaskEntity taskEntity, UserEntity userEntity, String type, String status, String priority, String sprint) {
        final ProjectEntity project = taskEntity.getProject();
        taskEntity.setKey(generateKey(project));
        taskEntity.setType(entryDAO.getEntryByEntryTypeAndValueAndProject(EntryType.TYPE, type, project).get());
        taskEntity.setStatus(entryDAO.getEntryByEntryTypeAndValueAndProject(EntryType.STATUS, status, project).get());
        taskEntity.setPriority(entryDAO.getEntryByEntryTypeAndValueAndProject(EntryType.PRIORITY, priority, project).get());
        taskEntity.setSprint(sprintDAO.getByNameAndProject(sprint, project).get());
        taskEntity.setCreator(userEntity);
        taskEntity.setWatchers(Sets.newHashSet(userEntity));
        taskEntity.setCreationDate(DateUtils.getCurrentTimestamp());
        taskEntity.setUpdateDate(DateUtils.getCurrentTimestamp());
        log.debug("Saving task with name: {} and key: {}", taskEntity.getName(), taskEntity.getKey());
        return taskRepository.save(taskEntity);
    }
    
    private String generateKey(ProjectEntity projectEntity) {
        final String projectName = projectEntity.getName();
        String key = combine(projectName, ThreadLocalRandom.current().nextInt(KEY_MIN_RANGE, KEY_MAX_RANGE));
        while ( getTaskByKey(key, projectEntity).isPresent() ) {
            log.debug("Task with key: {} already exists in project: {}. Generating another one", key, projectName);
            key = combine(projectName, ThreadLocalRandom.current().nextInt(KEY_MIN_RANGE, KEY_MAX_RANGE));
        }
        return key;
    }
    
    private String combine(String projectName, int number) {
        return projectName.toUpperCase().concat(KEY_SEPARATOR).concat(String.valueOf(number));
    }
    
}
