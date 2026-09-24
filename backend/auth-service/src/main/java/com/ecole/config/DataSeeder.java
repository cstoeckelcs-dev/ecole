package com.ecole.config;

import com.ecole.model.Authority;
import com.ecole.model.User;
import com.ecole.repository.AuthorityRepository;
import com.ecole.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.super-admin.email:superadmin@ecole.com}")
    private String superAdminEmail;

    @Value("${app.super-admin.password:SuperAdmin123!}")
    private String superAdminPassword;

    @Override
    public void run(String... args) {
        Authority superAdminAuthority = authorityRepository.findByName("ROLE_SUPER_ADMIN")
            .orElseGet(() -> authorityRepository.save(
                Authority.builder().name("ROLE_SUPER_ADMIN").description("Super Administrator").build()
            ));

        if (userRepository.findByEmail(superAdminEmail).isEmpty()) {
            User superAdmin = User.builder()
                .email(superAdminEmail)
                .password(passwordEncoder.encode(superAdminPassword))
                .firstName("Super")
                .lastName("Admin")
                .phone("+000000000")
                .address("System")
                .city("System")
                .postalCode("00000")
                .country("System")
                .role(User.Role.SUPER_ADMIN)
                .authorities(new java.util.HashSet<>(java.util.Set.of(superAdminAuthority)))
                .build();
            userRepository.save(superAdmin);
            log.info("Seeded super admin account: {}", superAdminEmail);
        }
    }
}
