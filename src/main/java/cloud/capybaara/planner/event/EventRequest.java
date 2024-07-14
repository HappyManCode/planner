package cloud.capybaara.planner.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    private Date date;
    private String description;
    private Boolean completed;
}
