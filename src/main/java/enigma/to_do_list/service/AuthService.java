package enigma.to_do_list.service;

import enigma.to_do_list.utils.dto.AuthDto;
import enigma.to_do_list.utils.dto.AuthDto.RegisterRequest;
import enigma.to_do_list.utils.dto.AuthDto.AuthenticationRequest;
import enigma.to_do_list.utils.dto.AuthDto.AuthenticationResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
