package com.etna.primeencoderplus.controllers;

import com.etna.primeencoderplus.exception.CustomGlobalException;
import com.etna.primeencoderplus.services.VideoService;
import com.etna.primeencoderplus.utilities.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/encoder")
public class EncoderController {

    private final VideoService videoService;

    public EncoderController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping(value = "", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<Object> encodeVideo(@RequestParam("id") String id, @RequestPart("source") @Valid @NotNull @NotEmpty MultipartFile source) throws JSONException, CustomGlobalException {
        Boolean canEncode = videoService.manageVideoName(id, source);
        if (Boolean.TRUE.equals(canEncode)) {
            videoService.encodeVideo(id);
            return ResponseEntity.status(201).body(JsonUtils.jsonifyVideoMp4(id).toString());
        }
        return ResponseEntity.status(400).body("Existe");
    }
}
