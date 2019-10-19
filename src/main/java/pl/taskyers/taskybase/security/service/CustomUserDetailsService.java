package pl.taskyers.taskybase.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.entity.UserEntity;
import pl.taskyers.taskybase.core.repository.UserRepository;

import java.util.HashSet;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private UserRepository userRepository;
    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).get();
    
        if ( user == null ) {
            throw new UsernameNotFoundException("User not found");
        }
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(user.getUsername(),
                                                                       user.getPassword(),
                                                                       new HashSet<SimpleGrantedAuthority>());
        return userDetails;
    }
    
}
