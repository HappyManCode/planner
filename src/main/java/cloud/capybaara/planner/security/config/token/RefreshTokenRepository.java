package cloud.capybaara.planner.security.config.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    @Query(value = """
      select t from RefreshToken t inner join AppUser u\s
      on t.appUser.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<RefreshToken> findAllValidTokenByUser(Long id);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}

