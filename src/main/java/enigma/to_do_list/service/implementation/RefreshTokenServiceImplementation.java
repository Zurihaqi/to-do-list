package enigma.to_do_list.service.implementation;

import enigma.to_do_list.exception.InvalidRefreshTokenException;
import enigma.to_do_list.model.RefreshToken;
import enigma.to_do_list.model.User;
import enigma.to_do_list.repository.RefreshTokenRepository;
import enigma.to_do_list.security.JwtService;
import enigma.to_do_list.service.RefreshTokenService;
import enigma.to_do_list.utils.dto.AuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImplementation implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Override
    public RefreshToken createRefreshToken(User user) {
        return refreshTokenRepository.save(
                RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plus(7, java.time.temporal.ChronoUnit.DAYS))
                .build()
        );
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + "Refresh token is expired.");
        }
        return token;
    }

    @Override
    public AuthDto.RefreshTokenResponse refreshToken(String refreshToken) {
        if(refreshToken == null || refreshToken.isEmpty()) throw new InvalidRefreshTokenException("refresh token cannot be empty");

        String accessToken = refreshTokenRepository.findByToken(refreshToken)
                .map(this::verifyExpiration).map(RefreshToken::getUser)
                .map(jwtService::generateToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("invalid refresh token"));

        return new AuthDto.RefreshTokenResponse(accessToken);
    }
}
