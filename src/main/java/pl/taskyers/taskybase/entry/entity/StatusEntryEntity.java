package pl.taskyers.taskybase.entry.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "status_entries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusEntryEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_entry_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = ProjectEntity.class)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonManagedReference
    private ProjectEntity project;
    
    @Column(name = "value", nullable = false)
    private String value;
    
    @Column(name = "text_color", nullable = false)
    private String textColor;
    
    @Column(name = "background_color", nullable = false)
    private String backgroundColor;
    
}
