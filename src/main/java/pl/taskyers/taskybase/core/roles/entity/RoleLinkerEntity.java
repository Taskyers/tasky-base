package pl.taskyers.taskybase.core.roles.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "role_linkers")
@Getter
@Setter
@NoArgsConstructor
public class RoleLinkerEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_linker_id")
    private Long id;
    
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private UserEntity user;
    
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, targetEntity = ProjectEntity.class)
    @JoinColumn(name = "project_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private ProjectEntity project;
    
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, targetEntity = RoleEntity.class)
    @JoinColumn(name = "role_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private RoleEntity role;
    
    @Column
    private boolean checked;
    
}
