package pl.taskyers.taskybase.registration.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.taskybase.core.users.entity.UserEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "verification_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationTokenEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_token_id")
    private Long id;
    
    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id", unique = true)
    private UserEntity user;
    
    @Column(unique = true)
    private String token;
    
}
