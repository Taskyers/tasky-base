package pl.taskyers.taskybase.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.taskyers.taskybase.core.roles.repository.RoleLinkerRepository;
import pl.taskyers.taskybase.core.roles.repository.RoleRepository;
import pl.taskyers.taskybase.core.slo.TokenSLO;
import pl.taskyers.taskybase.core.users.repository.UserRepository;
import pl.taskyers.taskybase.project.repository.ProjectInvitationTokenRepository;
import pl.taskyers.taskybase.project.repository.ProjectRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application.properties")
@AutoConfigureMockMvc
public abstract class IntegrationBase {
    
    @Autowired
    protected MockMvc mockMvc;
    
    @Autowired
    protected UserRepository userRepository;
    
    @Autowired
    protected ProjectRepository projectRepository;
    
    @Autowired
    protected RoleLinkerRepository roleLinkerRepository;
    
    @Autowired
    protected RoleRepository roleRepository;
    
    @Autowired
    protected ProjectInvitationTokenRepository projectInvitationTokenRepository;
    
    protected final ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    
    protected final String DEFAULT_USERNAME = "u1";
    
}
