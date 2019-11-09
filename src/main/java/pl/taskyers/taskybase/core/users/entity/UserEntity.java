package pl.taskyers.taskybase.core.users.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import pl.taskyers.taskybase.core.roles.entity.RoleLinkerEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    
    @ManyToMany(mappedBy = "users", targetEntity = ProjectEntity.class)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    Set<ProjectEntity> projects;
    
    @OneToMany(mappedBy = "user", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private Set<RoleLinkerEntity> roleLinkerEntities;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false, length = 60)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    @Column
    private String name;
    
    @Column
    private String surname;
    
    @Column(unique = true)
    private String email;
    
    @Column(columnDefinition = "boolean default false")
    private boolean enabled;
    
}
