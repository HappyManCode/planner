package cloud.capybaara.planner.appuser;

import lombok.AllArgsConstructor;
import cloud.capybaara.planner.email.EmailSender;
import cloud.capybaara.planner.email.EmailService;
import cloud.capybaara.planner.registration.registrationToken.ConfirmationToken;
import cloud.capybaara.planner.registration.registrationToken.ConfirmationTokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with email '%s' not found";
    private final static String EMAIL_ALREADY_TAKEN_MSG = "Email '%s' already taken";
    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    public List<AppUser> getUsers() {
        return appUserRepository.findAll();
    }

    public void addNewUser(AppUser appUser) {
        Optional<AppUser> userOptional = appUserRepository.findAppUserByEmail(appUser.getEmail());

        if (userOptional.isPresent()) {
            throw new IllegalStateException(String.format(USER_NOT_FOUND_MSG, appUser.getEmail()));
        }

        appUserRepository.save(appUser);
    }

    public void deleteUser(Long userId) {
        boolean exists = appUserRepository.existsById(userId);

        if (!exists) {
            throw new IllegalStateException("user with id " + userId + " does not exists");
        }

        appUserRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findAppUserByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser user) {
        boolean userExists = appUserRepository.findAppUserByEmail(user.getEmail()).isPresent();

        if (userExists) {
            user = appUserRepository.findAppUserByEmail(user.getEmail()).orElseThrow();

            if (!user.getEnabled()) {
                confirmationTokenService.removeConfirmationToken(user);

                String token = createConfirmationToken(user);

                String link = "http://localhost/api/v1/auth/registration/confirm?token=" + token;

                emailSender.send(user.getEmail(), EmailService.buildConfirmationEmail(user.getName(), link));

                return token;
            } else {
                throw new IllegalStateException(String.format(EMAIL_ALREADY_TAKEN_MSG, user.getEmail()));
            }
        }

        appUserRepository.save(user);

        return createConfirmationToken(user);
    }

    private String createConfirmationToken (AppUser user) {
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
