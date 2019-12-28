package pl.taskyers.taskybase.project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.taskyers.taskybase.project.slo.BoardSLO;

import static pl.taskyers.taskybase.project.slo.BoardSLO.BOARD_PREFIX;
import static pl.taskyers.taskybase.project.slo.BoardSLO.GET_BY_PROJECT_NAME;

@RestController
@RequestMapping(value = BOARD_PREFIX)
@AllArgsConstructor
public class BoardRestController {
    
    private final BoardSLO boardSLO;
    
    @RequestMapping(value = GET_BY_PROJECT_NAME, method = RequestMethod.GET)
    public ResponseEntity getBoard(@PathVariable String projectName) {
        return boardSLO.getBoard(projectName);
    }
    
}
