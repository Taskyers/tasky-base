package pl.taskyers.taskybase.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pl.taskyers.taskybase.core.users.entity.UserEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "projects")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;
    
    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "owner_id")
    private UserEntity owner;
    
    @ManyToMany(targetEntity = UserEntity.class)
    @JoinTable(name = "project_user",
            joinColumns = { @JoinColumn(name = "project_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") })
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<UserEntity> users;
    
    @Column(unique = true)
    private String name;
    
    @Column
    private String description;
    
}
