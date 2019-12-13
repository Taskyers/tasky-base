package pl.taskyers.taskybase.core.roles;

import org.springframework.http.ResponseEntity;

/**
 * Interface for entry authentication
 *
 * @author Marcin Ruszkiewicz
 */
public interface EntryEndpoint {
    
    /**
     * Check if user has proper role for operation on entry
     *
     * @param projectName project's name
     * @return status 403 if user has no permission, status 404 if project was not found, otherwise status 200
     * @since 0.0.3
     */
    ResponseEntity hasProperRoleOnEntry(String projectName);
    
}
