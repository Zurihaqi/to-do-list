package enigma.to_do_list.service.implementation;

import enigma.to_do_list.exception.UnauthorizedRoleException;
import enigma.to_do_list.model.User;
import enigma.to_do_list.repository.UserRepository;
import enigma.to_do_list.security.UserSecurity;
import enigma.to_do_list.service.UserService;
import enigma.to_do_list.utils.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSecurity userSecurity;

    @Override
    public void updateName(Integer id, String name) {
        UserDto userDto = getById(id);
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new RuntimeException("user with id " + id + " not found"));

        if(!Objects.equals(userSecurity.getCurrentUserRole(), "ADMIN") && !Objects.equals(id, userSecurity.getCurrentUserId())) throw new UnauthorizedRoleException("you can't update other user's name");
        if(name == null || name.isEmpty() || name.isBlank()) throw new RuntimeException("name cannot be empty");

        user.setUsername(name);
        userRepository.save(user);
    }

    @Override
    public void updateEmail(Integer id, String email) {
        UserDto userDto = getById(id);
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new RuntimeException("user with id " + id + " not found"));

        if(!Objects.equals(userSecurity.getCurrentUserRole(), "ADMIN") && !Objects.equals(id, userSecurity.getCurrentUserId())) throw new UnauthorizedRoleException("only Admin can update other user's email");
        if(email == null || email.isEmpty() || email.isBlank()) throw new RuntimeException("email cannot be empty");

        User userWithEmail = userRepository.findByEmail(email).orElse(null);

        if(userWithEmail != null && !Objects.equals(id, userWithEmail.getId())) throw new RuntimeException("email " + email + " is already registered");

        user.setEmail(email);

        userRepository.save(user);
    }

    @Override
    public void updatePassword(Integer id, String password) {
        UserDto userDto = getById(id);
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new RuntimeException("user with id " + id + " not found"));

        if(!Objects.equals(id, userSecurity.getCurrentUserId())) throw new UnauthorizedRoleException("you can't update other user's password");
        if(password == null || password.isBlank() || password.isEmpty()) throw new RuntimeException("password cannot be empty");

        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
    }

    @Override
    public void updateRole(Integer id, String role) {
        UserDto userDto = getById(id);
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new RuntimeException("user with id " + id + " not found"));

        if(!Objects.equals(userSecurity.getCurrentUserRole(), "ADMIN")) throw new UnauthorizedRoleException("only Admin can update other user's role");
        if(role == null || role.isEmpty() || role.isBlank()) throw new RuntimeException("role cannot be empty");
        if(!role.equals("ADMIN") && !role.equals("USER")) throw new RuntimeException("role must be ROLE_ADMIN or ROLE_USER");
    }

    @Override
    public Page<UserDto> getAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        return users.map(user -> UserDto.builder()
                .id(user.getId())
                .username(user.getActualUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build());
    }

    @Override
    public UserDto getById(Integer id) {
        if(!Objects.equals(userSecurity.getCurrentUserRole(), "ADMIN") && !Objects.equals(id, userSecurity.getCurrentUserId())) throw new UnauthorizedRoleException("you can't get other user's data");
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user with id " + id + " not found"));
        return UserDto.builder()
                .id(user.getId())
                .username(user.getActualUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public void delete(Integer id) {
        if(!Objects.equals(userSecurity.getCurrentUserRole(), "ADMIN") && !Objects.equals(id, userSecurity.getCurrentUserId())) throw new UnauthorizedRoleException("you can't delete other user's data");
        if(!userRepository.existsById(id)) throw new RuntimeException("user with id " + id + " not found");
        userRepository.deleteById(id);
    }
}
