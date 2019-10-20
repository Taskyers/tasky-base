package pl.taskyers.taskybase.recovery.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.taskybase.core.entity.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "password_recovery_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRecoveryTokenEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "password_recovery_token_id")
    private Long id;
    
    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id", unique = true)
    private UserEntity user;
    
    @Column(unique = true)
    private String token;
    
}
