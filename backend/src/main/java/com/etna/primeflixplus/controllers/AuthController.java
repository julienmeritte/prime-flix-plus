package com.etna.primeflixplus.controllers;


import com.etna.primeflixplus.dtos.TokenDto;
import com.etna.primeflixplus.dtos.RegisterDto;
import com.etna.primeflixplus.dtos.ValidationMailDto;
import com.etna.primeflixplus.entities.User;
import com.etna.primeflixplus.exception.CustomGlobalException;
import com.etna.primeflixplus.services.AuthService;
import com.etna.primeflixplus.services.MailService;
import com.etna.primeflixplus.utilities.Constants;
import com.etna.primeflixplus.utilities.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    private final MailService mailService;

    private final HttpHeaders headers;

    public AuthController(AuthService authService, MailService mailService) {
        this.authService = authService;
        this.mailService = mailService;
        this.headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, Constants.HEADER_CONTENT_TYPE);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterDto registerDto) throws CustomGlobalException, JSONException {
        User user = authService.registerUser(registerDto);
        mailService.sendVerificationMail(user);
        return ResponseEntity.status(201).headers(headers).body(JsonUtils.jsonifyUser(user).toString());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticate(@RequestBody RegisterDto registerDto) throws JSONException, CustomGlobalException {
        TokenDto token = authService.authenticateUser(registerDto.getMail(), registerDto.getPassword());
        return ResponseEntity.status(200).headers(headers).body(JsonUtils.jsonifyToken(token).toString());
    }

    @GetMapping("/refresh")
    public ResponseEntity<Object> refresh(HttpServletRequest request) throws CustomGlobalException, JSONException {
        TokenDto token = authService.refreshToken(request);
        return ResponseEntity.status(200).headers(headers).body(JsonUtils.jsonifyToken(token).toString());
    }

    @PostMapping("/validation")
    public ResponseEntity<Object> validationMail(@RequestBody ValidationMailDto validationMailDto) throws CustomGlobalException, JSONException {
        authService.validateCodeMail(validationMailDto.getCode());
        return ResponseEntity.status(200).headers(headers).body(new JSONObject().put("message", "OK").toString());
    }

    // TODO: Confirm Password

    // TODO: Validate Mail

    // TODO: Template
}
