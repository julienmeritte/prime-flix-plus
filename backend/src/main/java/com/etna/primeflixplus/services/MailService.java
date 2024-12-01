package com.etna.primeflixplus.services;

import com.etna.primeflixplus.entities.User;
import com.etna.primeflixplus.exception.CustomGlobalException;
import com.etna.primeflixplus.exception.CustomMessageException;
import com.etna.primeflixplus.repositories.UserRepository;
import com.etna.primeflixplus.utilities.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final Environment env;

    public MailService(JavaMailSender javaMailSender, PasswordEncoder passwordEncoder, UserRepository userRepository, Environment env) {
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.env = env;
    }

    public void sendMail(String destination, String subject, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(destination);
        mail.setSubject(subject);
        mail.setText(message);
        mail.setFrom("noreply.primeflixplus@gmail.com");
        javaMailSender.send(mail);
        log.info("Mail envoyé");
    }


    public void sendVerificationMail(User user) throws CustomGlobalException {
        String token = passwordEncoder.encode(Constants.STATIC_MAILING_TOKEN + user.getId().toString());
        user.setVerificationCode(token);
        userRepository.save(user);
        try {
            sendMail(user.getMail(), "Validation email Primeflix+", "Voici votre code de validation: " +
                    env.getProperty("primeflix.frontend.address") + Constants.ADDRESS_MAIL_VALIDATION + token);
        } catch (Exception e) {
            throw new CustomGlobalException(HttpStatus.INTERNAL_SERVER_ERROR, CustomMessageException.USER_SEND_MAIL_VERIFICATION);
        }
    }
}
