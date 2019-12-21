package pl.taskyers.taskybase.task.dao;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.core.utils.UserUtils;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.entry.dao.EntryDAO;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.sprint.dao.SprintDAO;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;
import pl.taskyers.taskybase.task.ResolutionType;
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
        taskEntity.setResolution(ResolutionType.UNRESOLVED);
        log.debug("Saving task with name: {} and key: {}", taskEntity.getName(), taskEntity.getKey());
        return taskRepository.save(taskEntity);
    }
    
    @Override
    public Optional<TaskEntity> getTaskByKey(String key) {
        return taskRepository.findByKey(key);
    }
    
    @Override
    public Optional<TaskEntity> getTaskById(Long id) {
        return taskRepository.findById(id);
    }
    
    @Override
    public TaskEntity setAssignee(TaskEntity taskEntity, UserEntity newAssignee) {
        log.debug("Setting new assignee {} to task {}", UserUtils.getPersonals(newAssignee), taskEntity.getKey());
        taskEntity.setAssignee(newAssignee);
        setUpdateDate(taskEntity);
        return taskRepository.save(taskEntity);
    }
    
    @Override
    public TaskEntity addWatcher(TaskEntity taskEntity, UserEntity userEntity) {
        log.debug("Adding watcher {} to task {}", UserUtils.getPersonals(userEntity), taskEntity.getKey());
        taskEntity.getWatchers().add(userEntity);
        setUpdateDate(taskEntity);
        return taskRepository.save(taskEntity);
    }
    
    @Override
    public TaskEntity updateEntry(TaskEntity taskEntity, EntryEntity entryEntity) {
        final EntryType entryType = entryEntity.getEntryType();
        log.debug("Updating entry of type {} with value {} in project {}", entryType, entryEntity.getValue(),
                taskEntity.getProject().getName());
        
        if ( EntryType.STATUS.equals(entryType) ) {
            taskEntity.setStatus(entryEntity);
        } else if ( EntryType.TYPE.equals(entryType) ) {
            taskEntity.setType(entryEntity);
        } else {
            taskEntity.setPriority(entryEntity);
        }
        
        setUpdateDate(taskEntity);
        return taskRepository.save(taskEntity);
    }
    
    @Override
    public TaskEntity updateTask(TaskEntity taskEntity, String name, String description, String fixVersion, SprintEntity sprintEntity,
            ResolutionType resolution) {
        log.debug("Updating task with id {}", taskEntity.getId());
        taskEntity.setName(name);
        taskEntity.setDescription(description);
        taskEntity.setFixVersion(fixVersion);
        taskEntity.setSprint(sprintEntity);
        taskEntity.setResolution(resolution);
        setUpdateDate(taskEntity);
        return taskRepository.save(taskEntity);
    }
    
    private void setUpdateDate(TaskEntity taskEntity) {
        taskEntity.setUpdateDate(DateUtils.getCurrentTimestamp());
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
