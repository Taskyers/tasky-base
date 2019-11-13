package pl.taskyers.taskybase.project.slo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.slo.TokenSLO;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.project.entity.ProjectInvitationTokenEntity;
import pl.taskyers.taskybase.project.repository.ProjectInvitationTokenRepository;
import pl.taskyers.taskybase.recovery.entity.PasswordRecoveryTokenEntity;

import java.util.Optional;
import java.util.UUID;

@Service("projectInvitationTokenSLO")
@AllArgsConstructor
@Slf4j
public class ProjectInvitationTokenSLOImpl implements TokenSLO<ProjectInvitationTokenEntity> {
    
    private final ProjectInvitationTokenRepository projectInvitationTokenRepository;
    
    @Override
    public String generateToken() {
        String token = UUID.randomUUID().toString();
        while ( projectInvitationTokenRepository.findByToken(token).isPresent() ) {
            log.warn("Generated token already exists in database: " + token + ". Generating another one");
            token = UUID.randomUUID().toString();
        }
        return token;
    }
    
    @Override
    public String getToken(UserEntity userEntity) {
        return projectInvitationTokenRepository.findByUser(userEntity).isPresent() ?
                projectInvitationTokenRepository.findByUser(userEntity).get().getToken() : null;
    }
    
    @Override
    public ProjectInvitationTokenEntity getTokenEntity(String token) {
        return projectInvitationTokenRepository.findByToken(token).isPresent() ? projectInvitationTokenRepository.findByToken(token).get() : null;
    }
    
    @Deprecated
    @Override
    public void createToken(UserEntity userEntity) {
    }
    
    @Override
    public void createToken(UserEntity userEntity, ProjectEntity projectEntity) {
        ProjectInvitationTokenEntity projectInvitationTokenEntity;
        if ( projectInvitationTokenRepository.findByUser(userEntity).isPresent() ) {
            projectInvitationTokenEntity = projectInvitationTokenRepository.findByUser(userEntity).get();
            projectInvitationTokenEntity.setToken(generateToken());
            log.debug("Updating token for user: " + userEntity.getUsername());
        } else {
            projectInvitationTokenEntity = new ProjectInvitationTokenEntity();
            projectInvitationTokenEntity.setToken(generateToken());
            projectInvitationTokenEntity.setUser(userEntity);
            projectInvitationTokenEntity.setProject(projectEntity);
            log.debug("Generating new token for user: " + userEntity.getUsername());
        }
        projectInvitationTokenRepository.save(projectInvitationTokenEntity);
    }
    
    @Override
    public void deleteToken(String token) {
        Optional<ProjectInvitationTokenEntity> passwordRecoveryTokenEntity = projectInvitationTokenRepository.findByToken(token);
        passwordRecoveryTokenEntity.ifPresent(recoveryTokenEntity -> projectInvitationTokenRepository.deleteById(recoveryTokenEntity.getId()));
    }
    
}
