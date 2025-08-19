package com.example.hunter_point.config;

import com.example.hunter_point.entity.Role;
import com.example.hunter_point.entity.User;
import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.entity.enums.UserStatus;
import com.example.hunter_point.repository.RoleRepository;
import com.example.hunter_point.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (roleRepository.findByName(ERole.ADMIN).isEmpty()) {
            roleRepository.save(new Role(ERole.ADMIN));
        }
        if (roleRepository.findByName(ERole.USER).isEmpty()) {
            roleRepository.save(new Role(ERole.USER));
        }
        if (roleRepository.findByName(ERole.ALLOCATOR).isEmpty()) {
            roleRepository.save(new Role(ERole.ALLOCATOR));
        }

        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(ERole.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .emailVerified(true)
                    .build();

            userRepository.save(admin);
            System.out.println("✅ Admin user created: admin@gmail.com / admin123");
        }
    }
}
