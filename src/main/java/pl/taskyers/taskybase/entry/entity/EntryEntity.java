package pl.taskyers.taskybase.entry.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "entry_entities")
@Data
@NoArgsConstructor
public class EntryEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_entity_id")
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
    
    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type", nullable = false)
    private EntryType entryType;
    
}
