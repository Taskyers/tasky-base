package pl.taskyers.taskybase.core.roles.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class RoleEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    
    @OneToMany(mappedBy = "role", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RoleLinkerEntity> projectRoleLinkers;
    
    @Column(unique = true, name = "`key`")
    private String key;
    
    @Column
    private String description;
    
}
