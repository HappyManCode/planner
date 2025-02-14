package cloud.capybaara.planner.task;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {

    @Id
    @SequenceGenerator(
            name = "task_sequence",
            sequenceName = "task_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_sequence")
    private Long id;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskPriority taskPriority;
    private Boolean completed = false;
    private Long userId;

    public Task(String description, TaskPriority taskPriority, Boolean completed, Long userId) {
        this.description = description;
        this.taskPriority = taskPriority;
        this.completed = completed;
        this.userId = userId;
    }
}
