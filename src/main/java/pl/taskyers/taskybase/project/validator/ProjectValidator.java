package pl.taskyers.taskybase.project.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.project.slo.ProjectSLO;

@Component("projectValidator")
@AllArgsConstructor
@Slf4j
public class ProjectValidator implements Validator<ProjectEntity> {
    
    private final ProjectSLO projectSLO;
    
    @Override
    public void validate(ProjectEntity object, ValidationMessageContainer validationMessageContainer) {
        if ( projectSLO.getProjectEntityByName(object.getName()).isPresent() ) {
            String message = MessageCode.project_field_already_exists.getMessage("name", object.getName());
            validationMessageContainer.addError(message, "name");
            log.warn(message);
        }
    }
    
}
