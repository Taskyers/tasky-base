package pl.taskyers.taskybase.project.slo;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ResponseMessage;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.utils.UriUtils;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.project.converter.ProjectConverter;
import pl.taskyers.taskybase.project.dao.ProjectDAO;
import pl.taskyers.taskybase.project.dto.ProjectDTO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import static pl.taskyers.taskybase.project.dao.ProjectDAO.PROJECTS_LIMIT;

@Service
@AllArgsConstructor
public class AddingProjectSLOImpl implements AddingProjectSLO {
    
    private final ProjectDAO projectDAO;
    
    private final Validator<ProjectEntity> projectValidator;
    
    private final AuthProvider authProvider;
    
    @Override
    public ResponseEntity addNewProject(ProjectDTO projectDTO) {
        ProjectEntity projectEntity = ProjectConverter.convertFromDTO(projectDTO);
        if ( projectDAO.getAllProjectsByOwner(authProvider.getUserEntity())
                     .size() >= PROJECTS_LIMIT ) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage<>(MessageCode.project_limit.getMessage(PROJECTS_LIMIT), MessageType.ERROR));
        }
        ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        projectValidator.validate(projectEntity, validationMessageContainer, true);
        if ( validationMessageContainer.hasErrors() ) {
            return ResponseEntity.badRequest()
                    .body(validationMessageContainer.getErrors());
        }
        ProjectEntity savedProject = projectDAO.addNewProject(projectEntity);
        return ResponseEntity.created(UriUtils.createURIFromId(savedProject.getId()))
                .body(new ResponseMessage<>(MessageCode.project_created.getMessage(), MessageType.SUCCESS, savedProject));
    }
    
    @Override
    public boolean projectExistsByName(String name) {
        return projectDAO.getProjectEntityByName(name)
                .isPresent();
    }
    
}
