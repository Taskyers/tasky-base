package pl.taskyers.taskybase.task.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.taskybase.core.users.entity.UserEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
public class CommentEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    
    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity author;
    
    @ManyToOne(targetEntity = TaskEntity.class)
    @JoinColumn(nullable = false, name = "task_id")
    private TaskEntity task;
    
    @Column(nullable = false)
    private String content;
    
    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean edited;
    
}
