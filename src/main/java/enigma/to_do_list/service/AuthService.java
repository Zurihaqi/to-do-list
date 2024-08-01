package enigma.to_do_list.service;

import enigma.to_do_list.utils.dto.AuthDto;
import enigma.to_do_list.utils.dto.AuthDto.RegisterRequest;
import enigma.to_do_list.utils.dto.AuthDto.AuthenticationRequest;
import enigma.to_do_list.utils.dto.AuthDto.AuthenticationResponse;
import enigma.to_do_list.utils.dto.AuthDto.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
