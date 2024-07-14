package cloud.capybaara.planner.registration;

import lombok.AllArgsConstructor;
import cloud.capybaara.planner.auth.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping(path = "/api/v1/auth/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(registrationService.register(request));
    }

    @GetMapping(path = "confirm")
    public RedirectView confirm(@RequestParam("token") String token) {
        if (registrationService.confirmToken(token).isTokenExpired()) {
            return new RedirectView("http://capybaara.cloud/email-confirmation-link-expired.html");
        }

        if (registrationService.confirmToken(token).isAlreadyConfirmed()) {
            return new RedirectView("http://capybaara.cloud/email-already-confirmed.html");
        }

        return new RedirectView("http://capybaara.cloud/email-confirmed.html");
    }
}
