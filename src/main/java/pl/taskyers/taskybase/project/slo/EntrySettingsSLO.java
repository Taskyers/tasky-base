package pl.taskyers.taskybase.project.slo;

import org.springframework.http.ResponseEntity;
import pl.taskyers.taskybase.entry.dto.CustomizableEntryDTO;

public interface EntrySettingsSLO {
    
    String STATUSES_PREFIX = "/secure/projects/settings/entries";
    
    String GET_BY_ENTRY_ID = "/{id}";
    
    String GET_BY_PROJECT_NAME = "/{projectName}";
    
    String GET_BY_PROJECT_NAME_AND_ENTRYTYPE = GET_BY_PROJECT_NAME + "/{type}";
    
    String GET_BY_PROJECT_NAME_AND_VALUE_AND_ENTRYTYPE = GET_BY_PROJECT_NAME + "/{value}/{type}";
    
    /**
     * Create new customizable entry from DTO
     *
     * @param projectName          project
     * @param customizableEntryDTO dto
     * @return status 404 if project is not found, 403 if user has no proper role, 400 if entry with value already exists in project, 201 if entry was created
     * @since 0.0.3
     */
    ResponseEntity createNewEntry(String projectName, CustomizableEntryDTO customizableEntryDTO);
    
    /**
     * Update existing customizable entry by id and DTO
     *
     * @param id                   entry id
     * @param customizableEntryDTO dto
     * @return status 404 if entry is not found, 403 if user has no proper role, 400 if entry with value already exists in project, 200 if entry was updated
     * @since 0.0.3
     */
    ResponseEntity updateEntry(Long id, CustomizableEntryDTO customizableEntryDTO);
    
    /**
     * Delete existing entry by id
     *
     * @param id entry id
     * @return status 404 if entry is not found, 403 if user has no proper role, 200 if entry was deleted
     * @since 0.0.3
     */
    ResponseEntity deleteEntry(Long id);
    
    /**
     * Check if value already exists in project
     *
     * @param value       value as string
     * @param projectName project's name
     * @return true if value exists, otherwise false
     * @since 0.0.3
     */
    ResponseEntity doesValueExistInProject(String value, String projectName, String type);
    
    /**
     * Check if user has proper role for operation on entry
     *
     * @param projectName project's name
     * @param type        entry type value
     * @return status 403 if user has no permission, status 404 if project was not found or entry type value is not valid, otherwise status 200
     * @since 0.0.3
     */
    ResponseEntity hasProperRoleOnEntry(String projectName, String type);
    
}
