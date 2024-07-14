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
public class TaskRequest {

    private String description;
    @Enumerated(EnumType.STRING)
    private TaskPriority taskPriority;
    private Boolean completed;
}
