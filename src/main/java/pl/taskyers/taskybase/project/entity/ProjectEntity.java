package pl.taskyers.taskybase.project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import pl.taskyers.taskybase.core.roles.entity.RoleLinkerEntity;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
public class ProjectEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;
    
    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "owner_id")
    @JsonManagedReference
    private UserEntity owner;
    
    @OneToMany(mappedBy = "project", targetEntity = SprintEntity.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private Set<SprintEntity> sprints;
    
    @ManyToMany(targetEntity = UserEntity.class)
    @JoinTable(name = "project_user",
            joinColumns = { @JoinColumn(name = "project_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") })
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<UserEntity> users;
    
    @OneToMany(mappedBy = "project", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private Set<RoleLinkerEntity> roleLinkerEntities;
    
    @OneToMany(mappedBy = "project", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private Set<EntryEntity> entryEntities;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column
    private String description;
    
    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
}
