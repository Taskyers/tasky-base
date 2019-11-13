package pl.taskyers.taskybase.project.slo;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ResponseMessage;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.utils.UriUtils;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.project.converter.ProjectConverter;
import pl.taskyers.taskybase.project.dto.ProjectDTO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

@Service
@AllArgsConstructor
public class AddingProjectSLOImpl implements AddingProjectSLO {
    
    private final ProjectSLO projectSLO;
    
    private final Validator<ProjectEntity> projectValidator;
    
    @Override
    public ResponseEntity addNewProject(ProjectDTO projectDTO) {
        ProjectEntity projectEntity = ProjectConverter.convertFromDTO(projectDTO);
        ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        projectValidator.validate(projectEntity, validationMessageContainer);
        if ( validationMessageContainer.hasErrors() ) {
            return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
        }
        ProjectEntity savedProject = projectSLO.addNewProject(projectEntity);
        return ResponseEntity.created(UriUtils.createURIFromId(savedProject.getId()))
                .body(new ResponseMessage<>(MessageCode.project_created.getMessage(), MessageType.SUCCESS, savedProject));
    }
    
    @Override
    public boolean projectExistsByName(String name) {
        return projectSLO.getProjectEntityByName(name).isPresent();
    }
    
}