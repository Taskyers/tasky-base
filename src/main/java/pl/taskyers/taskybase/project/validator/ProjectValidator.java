package pl.taskyers.taskybase.project.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    public void validate(ProjectEntity object, ValidationMessageContainer validationMessageContainer, boolean checkForDuplicates) {
        final String name = object.getName();
        String message = null;
        if ( checkForDuplicates && projectSLO.getProjectEntityByName(name).isPresent() ) {
            message = MessageCode.project_field_already_exists.getMessage("name", name);
        } else if ( StringUtils.isBlank(name) ) {
            message = MessageCode.field_empty.getMessage("Name");
        }
        if ( message != null ) {
            validationMessageContainer.addError(message, "name");
            log.warn(message);
        }
    }
    
}
