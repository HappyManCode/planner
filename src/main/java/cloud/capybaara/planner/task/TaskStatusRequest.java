package cloud.capybaara.planner.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusRequest {
    private Long id;
    private Boolean completed;
}
