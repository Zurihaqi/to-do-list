package enigma.to_do_list.service;

import enigma.to_do_list.model.RefreshToken;
import enigma.to_do_list.model.User;
import enigma.to_do_list.utils.dto.AuthDto;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);

    Optional<RefreshToken> findByToken(String token);

    RefreshToken verifyExpiration(RefreshToken token);

    AuthDto.RefreshTokenResponse refreshToken(String refreshToken);
}
