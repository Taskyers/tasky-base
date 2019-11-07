package pl.taskyers.taskybase.core.roles.constants;

/**
 * Interface which contains all roles in system as String.
 * Roles here and in database must be the same
 *
 * @author Jakub Sildatk
 */
public interface Roles {
    
    /**
     * @since 0.0.3
     */
    String SETTINGS_MANAGE_USERS = "settings.manage.users";
    
    /**
     * @since 0.0.3
     */
    String SETTINGS_EDIT_PROJECT = "settings.edit.project";
    
    /**
     * @since 0.0.3
     */
    String SETTINGS_DELETE_PROJECT = "settings.delete.project";
    
}
