package cloud.capybaara.planner.registration;

import lombok.AllArgsConstructor;
import cloud.capybaara.planner.auth.AuthenticationResponse;
import cloud.capybaara.planner.email.EmailService;
import cloud.capybaara.planner.registration.registrationToken.ConfirmationToken;
import cloud.capybaara.planner.registration.registrationToken.ConfirmationTokenService;
import cloud.capybaara.planner.appuser.AppUser;
import cloud.capybaara.planner.appuser.AppUserRole;
import cloud.capybaara.planner.appuser.AppUserService;
import cloud.capybaara.planner.email.EmailSender;
import cloud.capybaara.planner.security.config.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final static String EMAIL_NOT_VALID_MSG = "Email '%s' not valid";
    private final static String TOKEN_NOT_FOUND_MSG = "Token not found";
    private final static String EMAIL_ALREADY_CONFIRMED_MSG = "Email '%s' already confirmed";
    private final static String TOKEN_EXPIRED_MSG = "Token expired";
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(RegistrationRequest request) {
        boolean isValidEmail = EmailValidator.validateEmail(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException(String.format(EMAIL_NOT_VALID_MSG, request.getEmail()));
        }

        var user = AppUser.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .appUserRole(AppUserRole.USER)
                .locked(false)
                .enabled(false)
                .build();

        String token = appUserService.signUpUser(user);

        String link = "http://capybaara.cloud/api/v1/auth/registration/confirm?token=" + token;

        emailSender.send(user.getEmail(), EmailService.buildConfirmationEmail(user.getName(), link));

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public ConfirmationEmailResponse confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException(TOKEN_NOT_FOUND_MSG));

        ConfirmationEmailResponse confirmationEmailResponse = new ConfirmationEmailResponse();

        if (confirmationToken.getConfirmedAt() != null) {
            confirmationEmailResponse.setAlreadyConfirmed(true);

            throw new IllegalStateException(String.format(
                    EMAIL_ALREADY_CONFIRMED_MSG,
                    confirmationToken.getAppUser().getEmail()
            ));
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            confirmationEmailResponse.setTokenExpired(true);

            throw new IllegalStateException(TOKEN_EXPIRED_MSG);
        }

        confirmationEmailResponse.setConfirmed(true);

        confirmationTokenService.setConfirmedAt(token);

        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());

        return confirmationEmailResponse;
    }
}
