package pl.taskyers.taskybase.security.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.users.repository.UserRepository;
import pl.taskyers.taskybase.security.exception.UserNotEnabledException;

import java.util.HashSet;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        
        if ( !user.isPresent() ) {
            throw new UsernameNotFoundException("User not found.");
        }
        if ( !user.get().isEnabled() ) {
            throw new UserNotEnabledException("User is not enabled.");
        }
        
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(),
                                                                      user.get().getPassword(),
                                                                      new HashSet<SimpleGrantedAuthority>());
        
    }
    
}
