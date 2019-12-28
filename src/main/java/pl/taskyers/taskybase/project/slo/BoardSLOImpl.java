package pl.taskyers.taskybase.project.slo;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ResponseMessage;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.entry.dao.EntryDAO;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
import pl.taskyers.taskybase.project.converter.BoardTaskConverter;
import pl.taskyers.taskybase.project.dao.ProjectDAO;
import pl.taskyers.taskybase.project.dto.BoardResponseData;
import pl.taskyers.taskybase.project.dto.BoardStatusDTO;
import pl.taskyers.taskybase.project.dto.BoardTaskDTO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;
import pl.taskyers.taskybase.task.dao.TaskDAO;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BoardSLOImpl implements BoardSLO {
    
    private final AuthProvider authProvider;
    
    private final ProjectDAO projectDAO;
    
    private final EntryDAO entryDAO;
    
    private final TaskDAO taskDAO;
    
    @Override
    public ResponseEntity getBoard(String projectName) {
        Optional<ProjectEntity> projectEntityOptional = projectDAO.getProjectEntityByName(projectName);
        if ( projectEntityOptional.isPresent() ) {
            final UserEntity userEntity = authProvider.getUserEntity();
            final ProjectEntity projectEntity = projectEntityOptional.get();
            if ( projectDAO.getProjectByNameAndUser(projectName, userEntity).isPresent() ||
                 projectDAO.getProjectByNameAndOwner(projectName, userEntity).isPresent() ) {
                SprintEntity sprintEntity = getCurrentSprint(projectEntity);
                if ( sprintEntity == null ) {
                    return ResponseEntity.ok(new ResponseMessage<>(MessageCode.sprint_not_current.getMessage(), MessageType.WARN));
                }
                final List<EntryEntity> statuses = entryDAO.getAllByProjectAndEntryType(projectEntity, EntryType.STATUS);
                final BoardResponseData boardResponseData = createBoardResponseData(statuses, sprintEntity, projectEntity);
                return ResponseEntity.ok(boardResponseData);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.WARN));
    }
    
    private SprintEntity getCurrentSprint(ProjectEntity projectEntity) {
        return projectEntity.getSprints()
                .stream()
                .filter(sprintEntity -> DateUtils.checkIfDateBetweenTwoDates(DateUtils.getCurrentDate(), sprintEntity.getStart(),
                        sprintEntity.getEnd()))
                .findFirst()
                .orElse(null);
    }
    
    private BoardResponseData createBoardResponseData(List<EntryEntity> statuses, SprintEntity sprint, ProjectEntity project) {
        BoardResponseData boardResponseData = new BoardResponseData();
        boardResponseData.setSprintName(sprint.getName());
        for ( EntryEntity entryEntity : statuses ) {
            List<TaskEntity> tasks = taskDAO.getAllTasksByProjectAndStatusAndSprint(project, entryEntity.getValue(), sprint);
            List<BoardTaskDTO> taskDTOs = new ArrayList<>();
            for ( TaskEntity taskEntity : tasks ) {
                taskDTOs.add(BoardTaskConverter.convertToDTO(taskEntity));
            }
            boardResponseData.getStatuses().add(new BoardStatusDTO(entryEntity.getValue(), taskDTOs));
        }
        return boardResponseData;
    }
    
}
