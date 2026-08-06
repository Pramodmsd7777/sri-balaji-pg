package com.sribalajipg.controller;

import com.sribalajipg.entity.Role;
import com.sribalajipg.entity.User;
import com.sribalajipg.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Runs once on startup. Creates the very first ADMIN account ONLY if:
//   1. no ADMIN account exists yet in the database, AND
//   2. ADMIN_BOOTSTRAP_MOBILE and ADMIN_BOOTSTRAP_PASSWORD env vars are set.
//
// This means the admin password never lives in application.yml or in source code —
// you set it as a one-time environment variable on your host/deploy platform, start the app once,
// then REMOVE those two env vars (Render/Railway/etc. call this "unsetting" or deleting the var).
// After that, the admin logs in normally through /api/auth/login and — ideally — changes
// their password via a future "change password" endpoint.
@Component
public class AdminBootstrapRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrapRunner.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_BOOTSTRAP_MOBILE:}")
    private String bootstrapMobile;

    @Value("${ADMIN_BOOTSTRAP_PASSWORD:}")
    private String bootstrapPassword;

    public AdminBootstrapRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        boolean adminExists = userRepository.existsByRole(Role.ADMIN);

        if (adminExists) {
            return; // already bootstrapped — do nothing, every startup after the first is a no-op
        }

        if (bootstrapMobile.isBlank() || bootstrapPassword.isBlank()) {
            log.warn("No ADMIN account exists yet, and ADMIN_BOOTSTRAP_MOBILE / "
                    + "ADMIN_BOOTSTRAP_PASSWORD are not set. Set both env vars and restart "
                    + "the app once to create the first admin account.");
            return;
        }

        if (bootstrapPassword.length() < 8) {
            log.error("ADMIN_BOOTSTRAP_PASSWORD is too short (min 8 characters). Admin account NOT created.");
            return;
        }

        User admin = new User();
        admin.setMobileNumber(bootstrapMobile);
        admin.setPasswordHash(passwordEncoder.encode(bootstrapPassword));
        admin.setFullName("Admin");
        admin.setRole(Role.ADMIN);
        admin.setActive(true);
        userRepository.save(admin);

        log.warn("First ADMIN account created for mobile number {}. "
                + "You can now remove the ADMIN_BOOTSTRAP_MOBILE and ADMIN_BOOTSTRAP_PASSWORD "
                + "env vars — they are no longer needed and should not stay set.", bootstrapMobile);
    }
}
