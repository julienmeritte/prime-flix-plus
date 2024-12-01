package com.etna.primeflixplus.controllers;

import com.etna.primeflixplus.dtos.ProfileCreationDto;
import com.etna.primeflixplus.dtos.ProfileModificationDto;
import com.etna.primeflixplus.entities.Profile;
import com.etna.primeflixplus.entities.User;
import com.etna.primeflixplus.exception.CustomGlobalException;
import com.etna.primeflixplus.services.AuthService;
import com.etna.primeflixplus.services.ProfileService;
import com.etna.primeflixplus.utilities.Constants;
import com.etna.primeflixplus.utilities.JsonUtils;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    private final AuthService authService;

    private final HttpHeaders headers;

    public ProfileController(ProfileService profileService, AuthService authService) {
        this.profileService = profileService;
        this.authService = authService;
        this.headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, Constants.HEADER_CONTENT_TYPE);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addProfile(HttpServletRequest request, @Valid @RequestBody ProfileCreationDto profileCreationDto) throws CustomGlobalException, JSONException {
        User user = authService.getCurrentUser(request);
        Profile profile = profileService.addBasicProfile(user, profileCreationDto);
        return ResponseEntity.status(201).headers(headers).body(JsonUtils.jsonifyProfile(profile).toString());
    }

    @GetMapping("")
    public ResponseEntity<Object> getProfiles(HttpServletRequest request) throws CustomGlobalException, JSONException {
        User user = authService.getCurrentUser(request);
        List<Profile> profiles = profileService.getProfiles(user);
        return ResponseEntity.status(200).headers(headers).body(JsonUtils.jsonifyProfileList(profiles).toString());
    }

    @PostMapping("/addMain")
    public ResponseEntity<Object> addMainProfile(HttpServletRequest request, @Valid @RequestBody ProfileCreationDto profileCreationDto) throws CustomGlobalException, JSONException {
        User user = authService.getCurrentUser(request);
        Profile profile = profileService.addMainProfile(user, profileCreationDto);
        return ResponseEntity.status(201).headers(headers).body(JsonUtils.jsonifyProfile(profile).toString());
    }

    @PutMapping("")
    public ResponseEntity<Object> modifyProfile(HttpServletRequest request, @Valid @RequestBody ProfileModificationDto profileModificationDto) throws CustomGlobalException, JSONException {
        User user = authService.getCurrentUser(request);
        Profile profile = profileService.updateProfile(user, profileModificationDto);
        return ResponseEntity.status(200).headers(headers).body(JsonUtils.jsonifyProfile(profile).toString());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProfile(@PathVariable(name = "id") Integer id) throws CustomGlobalException, JSONException {
        profileService.deleteProfile(id);
        return ResponseEntity.status(200).headers(headers).body("OK");
    }

    // TODO: Update Profile

    // TODO: Delete Profile

    // TODO: Add Young Profile
}
