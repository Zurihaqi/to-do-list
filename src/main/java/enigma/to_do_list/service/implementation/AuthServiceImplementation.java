package enigma.to_do_list.service.implementation;

import enigma.to_do_list.model.User;
import enigma.to_do_list.repository.UserRepository;
import enigma.to_do_list.security.JwtService;
import enigma.to_do_list.service.AuthService;
import enigma.to_do_list.utils.dto.AuthDto.RegisterRequest;
import enigma.to_do_list.utils.dto.AuthDto.AuthenticationRequest;
import enigma.to_do_list.utils.dto.AuthDto.AuthenticationResponse;
import enigma.to_do_list.utils.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<?> register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) throw new RuntimeException("email already registered");

        if(request.getEmail().isEmpty() || request.getEmail().isBlank()) throw new RuntimeException("email cannot be empty");
        if(request.getName().isEmpty() || request.getName().isBlank()) throw new RuntimeException("name cannot be empty");
        if(request.getPassword().isEmpty() || request.getPassword().isBlank()) throw new RuntimeException("password cannot be empty");

        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.ROLE_USER)
                .build();

        User savedUser = userRepository.save(user);
        savedUser.setPassword(null);

        return Response.renderJson(savedUser, "user created", HttpStatus.CREATED);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        if(request.getEmail().isBlank() || request.getEmail().isEmpty()) throw new RuntimeException("email cannot be empty");
        if(request.getPassword().isBlank() || request.getPassword().isEmpty()) throw new RuntimeException("email cannot be empty");

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("email is not registered"));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e){
            throw new BadCredentialsException("invalid email or password");
        }

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .build();
    }
}
