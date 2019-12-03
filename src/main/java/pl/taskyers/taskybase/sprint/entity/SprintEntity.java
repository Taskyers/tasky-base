package pl.taskyers.taskybase.sprint.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sprints")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SprintEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprint_id")
    private Long id;
    
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, targetEntity = ProjectEntity.class)
    @JoinColumn(name = "project_id", nullable = false)
    @ToString.Exclude
    @JsonManagedReference
    private ProjectEntity project;
    
    // TODO: one to many with tasks
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date start;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date end;
    
}
