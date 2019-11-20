package pl.taskyers.taskybase.core.users.slo;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.users.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserSLOImpl implements UserSLO {
    
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserEntity registerUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }
    
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
    
    @Override
    public Optional<UserEntity> getEntityById(Long id) {
        return userRepository.findById(id);
    }
    
    @Override
    public void flushRepository() {
        userRepository.flush();
    }
    
    @Override
    public List<UserEntity> findUsersByUsernameLike(String username) {
        return userRepository.findTop5ByUsernameIgnoreCaseContaining(username);
    }
    
}
