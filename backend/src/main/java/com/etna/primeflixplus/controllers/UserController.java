package com.etna.primeflixplus.controllers;

import com.etna.primeflixplus.dtos.PaginationDto;
import com.etna.primeflixplus.dtos.UserDto;
import com.etna.primeflixplus.entities.User;
import com.etna.primeflixplus.exception.CustomGlobalException;
import com.etna.primeflixplus.exception.CustomMessageException;
import com.etna.primeflixplus.services.AuthService;
import com.etna.primeflixplus.services.UserService;
import com.etna.primeflixplus.utilities.Constants;
import com.etna.primeflixplus.utilities.JsonUtils;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final AuthService authService;

    private final HttpHeaders headers;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
        this.headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, Constants.HEADER_CONTENT_TYPE);
    }

    @GetMapping("/me")
    public ResponseEntity<Object> me(HttpServletRequest request) throws CustomGlobalException, JSONException {
        User user = authService.getCurrentUser(request);
        return ResponseEntity.status(200).headers(headers).body(JsonUtils.jsonifyUserWithProfiles(user).toString());
    }

    @GetMapping("/get/all")
    public ResponseEntity<Object> getAllUsers() throws CustomGlobalException, JSONException {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.status(200).headers(headers).body(JsonUtils.jsonifyUserList(users).toString());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> modifyUser(@PathVariable("id") Integer id, @Valid @RequestBody UserDto userDto) throws JSONException, CustomGlobalException {
        if (userDto.getMail() == null && userDto.getRole() == null && userDto.getEnabled() == null && userDto.getPassword() == null)
            throw new CustomGlobalException(HttpStatus.FORBIDDEN, CustomMessageException.USER_MODIF_NO_MODIF);
        User user = userService.modifyUser(id, userDto);
        return ResponseEntity.status(200).headers(headers).body(JsonUtils.jsonifyUser(user).toString());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Integer id) throws CustomGlobalException {
        userService.deleteUser(id);
        return ResponseEntity.status(200).headers(headers).body("OK");
    }

}
