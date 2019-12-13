package pl.taskyers.taskybase.task.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;
import pl.taskyers.taskybase.task.ResolutionType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;
    
    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(nullable = false, name = "creator_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private UserEntity creator;
    
    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "assignee_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private UserEntity assignee;
    
    @ManyToMany(targetEntity = UserEntity.class)
    @JoinTable(name = "task_user",
            joinColumns = { @JoinColumn(name = "task_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") })
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    Set<UserEntity> watchers;
    
    @ManyToOne(targetEntity = ProjectEntity.class, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(nullable = false, name = "project_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private ProjectEntity project;
    
    @ManyToOne(targetEntity = SprintEntity.class, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "sprint_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private SprintEntity sprint;
    
    @OneToMany(mappedBy = "task", targetEntity = CommentEntity.class, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CommentEntity> comments;
    
    @OneToOne(targetEntity = EntryEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "status_id")
    private EntryEntity status;
    
    @OneToOne(targetEntity = EntryEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "priority_id")
    private EntryEntity priority;
    
    @OneToOne(targetEntity = EntryEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "type_id")
    private EntryEntity type;
    
    @Column(name = "`key`", nullable = false)
    private String key;
    
    @Column(nullable = false)
    private String name;
    
    @Column
    private String description;
    
    @Column(name = "fix_version")
    private String fixVersion;
    
    @Column
    @Enumerated(EnumType.STRING)
    private ResolutionType resolution;
    
    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    
}
