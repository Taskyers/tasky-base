package pl.taskyers.taskybase.project.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.project.dao.ProjectDAO;

import static pl.taskyers.taskybase.project.dao.ProjectDAO.PROJECTS_LIMIT;

@Component("projectValidator")
@AllArgsConstructor
@Slf4j
public class ProjectValidator implements Validator<ProjectEntity> {
    
    private final ProjectDAO projectDAO;
    
    private final AuthProvider authProvider;
    
    @Override
    public void validate(ProjectEntity object, ValidationMessageContainer validationMessageContainer, boolean checkForDuplicates) {
        final String name = object.getName();
        String message = null;
        final UserEntity owner = authProvider.getUserEntity();
        
        if ( projectDAO.getAllProjectsByOwner(owner)
                     .size() >= PROJECTS_LIMIT ) {
            message = MessageCode.project_limit.getMessage(PROJECTS_LIMIT);
        } else {
            if ( checkForDuplicates && projectDAO.getProjectEntityByName(name)
                    .isPresent() ) {
                message = MessageCode.project_field_already_exists.getMessage("name", name);
            } else if ( StringUtils.isBlank(name) ) {
                message = MessageCode.field_empty.getMessage("Name");
            }
        }
        if ( message != null ) {
            validationMessageContainer.addError(message, "name");
            log.warn(message);
        }
    }
    
}
