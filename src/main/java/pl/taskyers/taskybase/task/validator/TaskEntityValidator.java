package pl.taskyers.taskybase.task.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.task.dao.TaskDAO;
import pl.taskyers.taskybase.task.entity.TaskEntity;

@Component("taskEntityValidator")
@AllArgsConstructor
@Slf4j
public class TaskEntityValidator implements Validator<TaskEntity> {
    
    private final TaskDAO taskDAO;
    
    @Override
    public void validate(TaskEntity object, ValidationMessageContainer validationMessageContainer, boolean checkForDuplicates) {
        final String name = object.getName();
        final String projectName = object.getProject().getName();
        if ( checkForDuplicates && taskDAO.getTaskByNameAndProject(name, object.getProject()).isPresent() ) {
            log.warn("Task with name " + name + " already exists in project " + projectName);
            validationMessageContainer.addError(MessageCode.task_field_already_exists.getMessage("name", name, projectName), "name");
        }
    }
    
}
