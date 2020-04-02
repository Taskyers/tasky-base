package pl.taskyers.taskybase.project.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.taskybase.core.users.entity.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "project_invitation_tokens")
@Data
@NoArgsConstructor
public class ProjectInvitationTokenEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_invitation_token_id")
    private Long id;
    
    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id", unique = true)
    private UserEntity user;
    
    @OneToOne(targetEntity = ProjectEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "project_id", unique = true)
    private ProjectEntity project;
    
    @Column(unique = true)
    private String token;
    
}
