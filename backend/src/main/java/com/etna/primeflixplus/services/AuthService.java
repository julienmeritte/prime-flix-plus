package com.etna.primeflixplus.services;

import com.etna.primeflixplus.configuration.JwtTokenProvider;
import com.etna.primeflixplus.dtos.RegisterDto;
import com.etna.primeflixplus.dtos.TokenDto;
import com.etna.primeflixplus.entities.User;
import com.etna.primeflixplus.exception.CustomGlobalException;
import com.etna.primeflixplus.exception.CustomMessageException;
import com.etna.primeflixplus.repositories.ProfileRepository;
import com.etna.primeflixplus.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final MailService mailService;

    public AuthService(UserRepository userRepository, ProfileRepository profileRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, MailService mailService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mailService = mailService;
    }

    public User registerUser(RegisterDto registerDto) throws CustomGlobalException {
        if (userRepository.findByMail(registerDto.getMail()).isEmpty()) {
            User newUser = new User();
            newUser.setMail(registerDto.getMail());
            newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            userRepository.save(newUser);
            return newUser;
        } else {
            throw new CustomGlobalException(HttpStatus.CONFLICT, CustomMessageException.USER_REGISTER_ALREADY_EXISTS);
        }
    }

    public TokenDto authenticateUser(String mail, String password) throws CustomGlobalException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(mail, password));
            Optional<User> user = userRepository.findByMail(mail);
            if (user.isPresent()) {
                TokenDto tokenDto = new TokenDto();
                tokenDto.setRefresh(jwtTokenProvider.createToken(user.get().getMail(), user.get().getRole(), Boolean.TRUE));
                tokenDto.setToken(jwtTokenProvider.createToken(user.get().getMail(), user.get().getRole(), Boolean.FALSE));
                return tokenDto;
            }
            throw new CustomGlobalException(HttpStatus.UNAUTHORIZED, CustomMessageException.USER_AUTH_FAILED);
        } catch (AuthenticationException | CustomGlobalException e) {
            throw new CustomGlobalException(HttpStatus.UNAUTHORIZED, CustomMessageException.USER_AUTH_FAILED);
        }
    }

    public TokenDto refreshToken(HttpServletRequest request) throws CustomGlobalException {
        String refresh = jwtTokenProvider.resolveToken(request);
        if (refresh != null && jwtTokenProvider.validateToken(refresh)) {
            User user = getCurrentUser(request);
            TokenDto tokenDto = new TokenDto();
            tokenDto.setRefresh(jwtTokenProvider.createToken(user.getMail(), user.getRole(), Boolean.TRUE));
            tokenDto.setToken(jwtTokenProvider.createToken(user.getMail(), user.getRole(), Boolean.FALSE));
            return tokenDto;
        }
        throw new CustomGlobalException(HttpStatus.UNAUTHORIZED, CustomMessageException.REFRESH_TOKEN_INVALID);
    }

    public User getCurrentUser(HttpServletRequest req) throws CustomGlobalException {
        try {
            Optional<User> user = userRepository.findByMail(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
            if (user.isEmpty())
                throw new CustomGlobalException(HttpStatus.BAD_REQUEST, CustomMessageException.USER_INVALID_TOKEN);
            return user.get();
        } catch (Exception e) {
            throw new CustomGlobalException(HttpStatus.BAD_REQUEST, CustomMessageException.USER_INVALID_TOKEN);
        }
    }

    public void validateCodeMail(String code) throws CustomGlobalException {
        try {
            Optional<User> user = userRepository.findUserByVerificationCode(code);
            if (user.isEmpty())
                throw new CustomGlobalException(HttpStatus.NOT_FOUND, CustomMessageException.USER_MAIL_VALIDATION_NOT_FOUND);
            else {
                if (user.get().getEnabled() == Boolean.TRUE)
                    throw new CustomGlobalException(HttpStatus.NOT_FOUND, CustomMessageException.USER_MAIL_VALIDATION_ALREADY_ACTIVATED);
                user.get().setEnabled(Boolean.TRUE);
                user.get().setVerificationCode(null);
                userRepository.save(user.get());
            }
        } catch (CustomGlobalException e) {
            if (e.getCustomMessage().equals(CustomMessageException.USER_MAIL_VALIDATION_NOT_FOUND))
                throw new CustomGlobalException(HttpStatus.NOT_FOUND, CustomMessageException.USER_MAIL_VALIDATION_NOT_FOUND);
            if (e.getCustomMessage().equals(CustomMessageException.USER_MAIL_VALIDATION_ALREADY_ACTIVATED))
                throw new CustomGlobalException(HttpStatus.NOT_FOUND, CustomMessageException.USER_MAIL_VALIDATION_ALREADY_ACTIVATED);
        } catch (Exception e) {
            throw new CustomGlobalException(HttpStatus.INTERNAL_SERVER_ERROR, CustomMessageException.USER_MAIL_VALIDATION);
        }
    }
}
