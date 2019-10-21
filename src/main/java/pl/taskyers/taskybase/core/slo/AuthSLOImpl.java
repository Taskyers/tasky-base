package pl.taskyers.taskybase.core.slo;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.entity.UserEntity;

@Service
@AllArgsConstructor
public class AuthSLOImpl implements AuthSLO {
    
    private final UserSLO userSLO;
    
    @Override
    public String getUserLogin() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    
    @Override
    public UserEntity getUserEntity() {
        return userSLO.getEntityByUsername(getUserLogin()).get();
    }
    
    @Override
    public String getUserEmail() {
        return getUserEntity().getEmail();
    }
    
    @Override
    public String getUserName() {
        return getUserEntity().getName();
    }
    
    @Override
    public String getUserSurname() {
        return getUserEntity().getSurname();
    }
    
    @Override
    public String getUserPersonal() {
        return getUserEntity().getName() + " " + getUserEntity().getSurname();
    }
    
}
