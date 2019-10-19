package pl.taskyers.taskybase.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private boolean postOnly = true;
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        
        if ( postOnly && !request.getMethod().equals("POST") ) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        
        LoginRequest loginRequest;
        try {
            BufferedReader reader = request.getReader();
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ( (line = reader.readLine()) != null ) {
                sb.append(line);
            }
            ObjectMapper mapper = new ObjectMapper();
            loginRequest = mapper.readValue(sb.toString(), LoginRequest.class);
        } catch ( Exception ex ) {
            throw new AuthenticationServiceException("Unable to read login credentials.");
        }
        
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());
        
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
    
    @Data
    private static class LoginRequest {
        
        String username;
        
        String password;
        
    }
    
}

