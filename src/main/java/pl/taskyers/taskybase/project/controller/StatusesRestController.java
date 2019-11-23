package pl.taskyers.taskybase.project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.taskybase.entry.dto.CustomizableEntryDTO;
import pl.taskyers.taskybase.project.slo.EntrySettingsSLO;

import static pl.taskyers.taskybase.project.slo.EntrySettingsSLO.*;

@RestController
@RequestMapping(value = STATUSES_PREFIX)
@AllArgsConstructor
public class StatusesRestController {
    
    private final EntrySettingsSLO entrySettingsSLO;
    
    @RequestMapping(value = GET_BY_PROJECT_NAME, method = RequestMethod.GET)
    public ResponseEntity entryPoint(@PathVariable String projectName) {
        return entrySettingsSLO.hasProperRoleOnEntry(projectName);
    }
    
    @RequestMapping(value = GET_BY_PROJECT_NAME, method = RequestMethod.POST)
    public ResponseEntity createNewEntry(@PathVariable String projectName, @RequestBody CustomizableEntryDTO customizableEntryDTO) {
        return entrySettingsSLO.createNewEntry(projectName, customizableEntryDTO);
    }
    
    @RequestMapping(value = GET_BY_ENTRY_ID, method = RequestMethod.PUT)
    public ResponseEntity updateEntry(@PathVariable Long id, @RequestBody CustomizableEntryDTO customizableEntryDTO) {
        return entrySettingsSLO.updateEntry(id, customizableEntryDTO);
    }
    
    @RequestMapping(value = GET_BY_ENTRY_ID, method = RequestMethod.DELETE)
    public ResponseEntity deleteEntry(@PathVariable Long id) {
        return entrySettingsSLO.deleteEntry(id);
    }
    
    @RequestMapping(value = GET_BY_PROJECT_NAME_AND_VALUE, method = RequestMethod.GET)
    public ResponseEntity doesValueExistInProject(@PathVariable String projectName, @PathVariable String value) {
        return entrySettingsSLO.doesValueExistInProject(value, projectName);
    }
    
}
