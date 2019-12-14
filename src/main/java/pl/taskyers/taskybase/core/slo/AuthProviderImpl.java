package pl.taskyers.taskybase.core.slo;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.users.dao.UserDAO;

import java.util.Set;

@Service
@AllArgsConstructor
public class AuthProviderImpl implements AuthProvider {
    
    private final UserDAO userDAO;
    
    @Override
    public String getUserLogin() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    
    @Override
    public UserEntity getUserEntity() {
        return userDAO.getEntityByUsername(getUserLogin()).get();
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
        return getUserName() + " " + getUserSurname();
    }
    
    @Override
    public Set<UserEntity> getUserEntityAsSet() {
        return Sets.newHashSet(getUserEntity());
    }
    
    @Override
    public boolean isLoggedIn() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !("anonymousUser").equals(authentication.getName());
    }
    
}
