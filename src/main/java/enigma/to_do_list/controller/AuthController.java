package enigma.to_do_list.controller;

import enigma.to_do_list.service.AuthService;
import enigma.to_do_list.service.RefreshTokenService;
import enigma.to_do_list.utils.dto.AuthDto;
import enigma.to_do_list.utils.dto.AuthDto.RegisterRequest;
import enigma.to_do_list.utils.dto.AuthDto.AuthenticationRequest;
import enigma.to_do_list.utils.dto.AuthDto.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {

        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestBody AuthDto.RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(request.getRefreshToken()));
    }
}
