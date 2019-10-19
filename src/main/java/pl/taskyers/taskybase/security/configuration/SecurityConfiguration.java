package pl.taskyers.taskybase.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import pl.taskyers.taskybase.security.filter.CustomAuthenticationFilter;
import pl.taskyers.taskybase.security.handler.RESTAuthenticationEntryPoint;
import pl.taskyers.taskybase.security.handler.RESTAuthenticationFailureHandler;
import pl.taskyers.taskybase.security.handler.RESTAuthenticationSuccessHandler;
import pl.taskyers.taskybase.security.service.CustomUserDetailsService;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private RESTAuthenticationEntryPoint restAuthenticationEntryPoint;
    
    @Autowired
    private RESTAuthenticationSuccessHandler restAuthenticationSuccessHandler;
    
    @Autowired
    private RESTAuthenticationFailureHandler restAuthenticationFailureHandler;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        
        auth.userDetailsService(customUserDetailsService);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/secure/**").authenticated()
                .and()
                .addFilterAt(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .and()
                .logout()
                .and()
                .cors()
                .configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
        
    }
    
    protected CustomAuthenticationFilter getAuthenticationFilter() {
        CustomAuthenticationFilter authFilter = new CustomAuthenticationFilter();
        try {
            authFilter.setAuthenticationManager(this.authenticationManagerBean());
            authFilter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
            authFilter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return authFilter;
    }
    
}
