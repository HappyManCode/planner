package cloud.capybaara.planner.task;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEditRequest {
    private Long id;
    private String description;
    private Boolean completed;
    @Enumerated(EnumType.STRING)
    private TaskPriority taskPriority;
}
