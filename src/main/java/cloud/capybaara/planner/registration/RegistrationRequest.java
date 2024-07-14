package cloud.capybaara.planner.registration;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    private String name;
    private String email;
    private String password;
}
