package com.etna.primeflixplus;

import com.etna.primeflixplus.entities.User;
import com.etna.primeflixplus.enums.UserRole;
import com.etna.primeflixplus.repositories.ProfileRepository;
import com.etna.primeflixplus.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@SpringBootApplication
public class PrimeflixPlusApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(PrimeflixPlusApplication.class, args);
    }

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final PasswordEncoder passwordEncoder;

    private final Environment env;

    public PrimeflixPlusApplication(UserRepository userRepository, ProfileRepository profileRepository, PasswordEncoder passwordEncoder, Environment env) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
    }

    @Override
    public void run(String... params) throws Exception {
        log.warn("ADRESSE BACKEND: " + env.getProperty("primeflix.backend.address"));
        log.warn("ADRESSE ENCODEUR: " + env.getProperty("primeflix.encoder.address"));
        log.warn("ADRESSE FRONTEND: " + env.getProperty("primeflix.frontend.address"));

        try {
            populateUsers();

            User support = new User();
            support.setMail("support@primeflixplus.com");
            support.setPassword(passwordEncoder.encode("support"));
            support.setRole(UserRole.ROLE_SUPPORT);
            support.setEnabled(Boolean.TRUE);
            userRepository.save(support);

            User admin = new User();
            admin.setMail("admin@primeflixplus.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(UserRole.ROLE_ADMIN);
            admin.setEnabled(Boolean.TRUE);
            userRepository.save(admin);
            log.info("FIN CONFIG DE LANCEMENT.");
        } catch (Exception e) {
            log.info("CONFIG DEJA PRESENTE");
        }
    }

    private void populateUsers() {
        String password = passwordEncoder.encode("user");
        populateUser("user@primeflixplus.com", password);
        populateUser("user1@primeflixplus.com", password);
        populateUser("user2@primeflixplus.com", password);
        populateUser("user3@primeflixplus.com", password);
        populateUser("user4@primeflixplus.com", password);
        populateUser("user5@primeflixplus.com", password);
        populateUser("user6@primeflixplus.com", password);
        populateUser("user7@primeflixplus.com", password);
        populateUser("user8@primeflixplus.com", password);
        populateUser("user9@primeflixplus.com", password);
        populateUser("user10@primeflixplus.com", password);
        populateUser("user11@primeflixplus.com", password);
        populateUser("user12@primeflixplus.com", password);
        populateUser("user13@primeflixplus.com", password);
        populateUser("user14@primeflixplus.com", password);
        populateUser("user15@primeflixplus.com", password);
        populateUser("user16@primeflixplus.com", password);
        populateUser("user17@primeflixplus.com", password);
        populateUser("user18@primeflixplus.com", password);
        populateUser("user19@primeflixplus.com", password);
    }

    private void populateUser(String mail, String password) {
        User user = new User();
        user.setMail(mail);
        user.setPassword(password);
        user.setRole(UserRole.ROLE_USER);
        user.setEnabled(Boolean.TRUE);
        userRepository.save(user);

    }

}
