package enigma.to_do_list.repository;

import enigma.to_do_list.model.RefreshToken;
import enigma.to_do_list.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}
