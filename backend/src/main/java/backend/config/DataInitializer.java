package backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import backend.entity.User;
import backend.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        createAdmin();
    }

    private void createAdmin() {

        if (userRepository
                .findByUsername("admin")
                .isPresent()) {

            return;
        }

        User admin = new User();

        admin.setUsername("admin");

        admin.setPassword(
                passwordEncoder.encode("admin123")
        );

        admin.setEmail("admin@bookkeeping.com");

        admin.setRole("ADMIN");

        admin.setEnabled(true);

        userRepository.save(admin);

        System.out.println(
                "Default admin account created."
        );
    }
}