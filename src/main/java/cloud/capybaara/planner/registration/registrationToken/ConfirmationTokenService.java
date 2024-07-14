package cloud.capybaara.planner.registration.registrationToken;

import lombok.AllArgsConstructor;
import cloud.capybaara.planner.appuser.AppUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public void removeConfirmationToken(AppUser user) {
        ConfirmationToken token = confirmationTokenRepository.findByAppUser(user).orElseThrow();
        confirmationTokenRepository.delete(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }
}
