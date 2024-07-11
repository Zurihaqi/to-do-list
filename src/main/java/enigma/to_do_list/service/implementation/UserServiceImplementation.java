package enigma.to_do_list.service.implementation;

import enigma.to_do_list.model.User;
import enigma.to_do_list.repository.UserRepository;
import enigma.to_do_list.service.UserService;
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

    @Override
    public User updateName(Integer id, String name) {
        User user = getById(id);

        if(name == null || name.isEmpty() || name.isBlank()) throw new RuntimeException("name cannot be empty");

        user.setName(name);
        return userRepository.save(user);
    }

    @Override
    public User updateEmail(Integer id, String email) {
        User user = getById(id);

        if(email == null || email.isEmpty() || email.isBlank()) throw new RuntimeException("email cannot be empty");

        User userWithEmail = userRepository.findByEmail(email).orElse(null);

        if(userWithEmail != null && !Objects.equals(id, userWithEmail.getId())) throw new RuntimeException("email " + email + " is already registered");

        user.setEmail(email);

        return userRepository.save(user);
    }

    @Override
    public User updatePassword(Integer id, String password) {
        User user = getById(id);

        if(password == null || password.isBlank() || password.isEmpty()) throw new RuntimeException("password cannot be empty");

        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User getById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("user with id " + id + " not found"));
    }

    @Override
    public void delete(Integer id) {
        if(!userRepository.existsById(id)) throw new RuntimeException("user with id " + id + " not found");
        userRepository.deleteById(id);
    }
}
