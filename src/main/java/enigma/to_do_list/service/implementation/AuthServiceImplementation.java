package enigma.to_do_list.service.implementation;

import enigma.to_do_list.exception.CredAlreadyExistException;
import enigma.to_do_list.model.RefreshToken;
import enigma.to_do_list.model.User;
import enigma.to_do_list.repository.RefreshTokenRepository;
import enigma.to_do_list.repository.UserRepository;
import enigma.to_do_list.security.JwtService;
import enigma.to_do_list.service.AuthService;
import enigma.to_do_list.service.RefreshTokenService;
import enigma.to_do_list.utils.dto.AuthDto.RegisterRequest;
import enigma.to_do_list.utils.dto.AuthDto.AuthenticationRequest;
import enigma.to_do_list.utils.dto.AuthDto.AuthenticationResponse;
import enigma.to_do_list.utils.dto.AuthDto.RegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        String validEmailFormat = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if (!request.getEmail().matches(validEmailFormat)) {
            throw new RuntimeException("Invalid email format");
        }
        if(userRepository.findByEmail(request.getEmail()).isPresent()) throw new CredAlreadyExistException("email already exists");
        if(userRepository.findByUsername(request.getUsername()).isPresent()) throw new CredAlreadyExistException("username already exists");

        if(request.getEmail().isEmpty() || request.getEmail().isBlank()) throw new RuntimeException("email cannot be empty");
        if(request.getUsername().isEmpty() || request.getUsername().isBlank()) throw new RuntimeException("username cannot be empty");
        if(request.getPassword().isEmpty() || request.getPassword().isBlank()) throw new RuntimeException("password cannot be empty");
        if(request.getPassword().length() < 8) throw new RuntimeException("password too short");

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.ROLE_USER)
                .build();

        User savedUser = userRepository.save(user);
        savedUser.setPassword(null);

        return RegisterResponse.builder()
                .username(savedUser.getActualUsername())
                .email(savedUser.getEmail())
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        if(request.getEmail().isBlank() || request.getEmail().isEmpty()) throw new RuntimeException("email cannot be empty");
        if(request.getPassword().isBlank() || request.getPassword().isEmpty()) throw new RuntimeException("email cannot be empty");

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new BadCredentialsException("invalid credentials"));

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            if(authentication.isAuthenticated()){
                RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElseGet(() -> refreshTokenService.createRefreshToken(user));
                return AuthenticationResponse.builder()
                        .accessToken(jwtService.generateToken(user))
                        .refreshToken(refreshToken.getToken())
                        .build();
            } else throw new BadCredentialsException("invalid credentials");
        } catch (BadCredentialsException e){
            throw new BadCredentialsException("invalid credentials");
        }
    }
}
