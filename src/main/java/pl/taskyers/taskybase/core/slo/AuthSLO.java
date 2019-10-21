package pl.taskyers.taskybase.core.slo;

import pl.taskyers.taskybase.core.users.entity.UserEntity;

public interface AuthSLO {
    
    String getUserLogin();
    
    UserEntity getUserEntity();
    
    String getUserEmail();
    
    String getUserName();
    
    String getUserSurname();
    
    String getUserPersonal();
    
}
