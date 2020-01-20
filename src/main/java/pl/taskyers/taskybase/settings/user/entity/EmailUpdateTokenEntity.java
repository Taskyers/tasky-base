package pl.taskyers.taskybase.settings.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.taskybase.core.users.entity.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "email_update_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailUpdateTokenEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_update_token_id")
    private Long id;
    
    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id", unique = true)
    private UserEntity user;
    
    @Column(unique = true)
    private String token;
    
    @Column(unique = true)
    private String email;
    
}
