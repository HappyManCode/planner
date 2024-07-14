package cloud.capybaara.planner.registration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationEmailResponse {

    private boolean isConfirmed = false;
    private boolean isTokenExpired = false;
    private boolean isAlreadyConfirmed = false;
}