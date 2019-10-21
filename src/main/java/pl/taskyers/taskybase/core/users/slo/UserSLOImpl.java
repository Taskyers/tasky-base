package pl.taskyers.taskybase.core.users.slo;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.users.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserSLOImpl implements UserSLO {
    
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void updatePassword(UserEntity userEntity, String password) {
        userEntity.setPassword(passwordEncoder.encode(password));
        userRepository.save(userEntity);
    }
    
    @Override
    public void enableUser(UserEntity userEntity) {
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
    }
    
    @Override
    public Optional<UserEntity> getEntityByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public Optional<UserEntity> getEntityByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
}
