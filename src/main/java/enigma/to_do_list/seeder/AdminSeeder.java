package enigma.to_do_list.seeder;

import enigma.to_do_list.model.User;
import enigma.to_do_list.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AdminSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.seeder.admin.email}")
    private String adminEmail;

    @Value("${application.seeder.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        seedAdmins();
    }

    private void seedAdmins(){
        if(userRepository.findByEmail(adminEmail).isPresent()) return;

        userRepository.save(
                User.builder()
                        .email(adminEmail)
                        .username("Admin")
                        .password(passwordEncoder.encode(adminPassword))
                        .role(User.Role.ROLE_ADMIN)
                .build()
        );
    }
}
