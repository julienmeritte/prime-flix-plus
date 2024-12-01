package com.etna.primeflixplus.configuration;

import com.etna.primeflixplus.exception.CustomGlobalException;
import com.etna.primeflixplus.exception.CustomMessageException;
import com.etna.primeflixplus.repositories.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) {
        com.etna.primeflixplus.entities.User user;
        try {
            Optional<com.etna.primeflixplus.entities.User> userOptional = userRepository.findByMail(username);
            if (userOptional.isPresent())
                user = userOptional.get();
            else
                throw new CustomGlobalException(HttpStatus.UNAUTHORIZED, CustomMessageException.USER_AUTH_FAILED);
        } catch (Exception e) {
            throw new CustomGlobalException(HttpStatus.UNAUTHORIZED, CustomMessageException.USER_AUTH_FAILED);
        }

        return User
                .withUsername(username)
                .password(user.getPassword())
                .authorities(user.getRole())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
