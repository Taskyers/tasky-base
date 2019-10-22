package pl.taskyers.taskybase.security.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import pl.taskyers.taskybase.security.filter.CustomAuthenticationFilter;
import pl.taskyers.taskybase.security.handler.RESTAuthenticationEntryPoint;
import pl.taskyers.taskybase.security.handler.RESTAuthenticationFailureHandler;
import pl.taskyers.taskybase.security.handler.RESTAuthenticationSuccessHandler;
import pl.taskyers.taskybase.security.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    private final RESTAuthenticationEntryPoint restAuthenticationEntryPoint;
    
    private final RESTAuthenticationSuccessHandler restAuthenticationSuccessHandler;
    
    private final RESTAuthenticationFailureHandler restAuthenticationFailureHandler;
    
    private final CustomUserDetailsService customUserDetailsService;
    
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
                .logout().deleteCookies("JSESSIONID").invalidateHttpSession(true)
                .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
                .and()
                .cors()
                .configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
        
    }
    
    private CustomAuthenticationFilter getAuthenticationFilter() {
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
