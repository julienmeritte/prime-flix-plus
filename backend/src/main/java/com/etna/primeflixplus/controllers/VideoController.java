package com.etna.primeflixplus.controllers;

import com.etna.primeflixplus.dtos.QualityDto;
import com.etna.primeflixplus.dtos.VideoDto;
import com.etna.primeflixplus.dtos.VideoGroupCreationDto;
import com.etna.primeflixplus.entities.Video;
import com.etna.primeflixplus.entities.VideoGroup;
import com.etna.primeflixplus.enums.VideoQuality;
import com.etna.primeflixplus.exception.CustomGlobalException;
import com.etna.primeflixplus.services.VideoEncodingService;
import com.etna.primeflixplus.services.VideoService;
import com.etna.primeflixplus.utilities.Constants;
import com.etna.primeflixplus.utilities.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/video")
public class VideoController {

    private final VideoService videoService;

    private final VideoEncodingService videoEncodingService;

    private final HttpHeaders headers;

    public VideoController(VideoService videoService, VideoEncodingService videoEncodingService) {
        this.videoService = videoService;
        this.videoEncodingService = videoEncodingService;
        this.headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, Constants.HEADER_CONTENT_TYPE);
    }

    @PostMapping("")
    public ResponseEntity<Object> sendCallEncoder() {
        /*String url = "http://localhost:8081/encoder";
        String response = this.restTemplate.getForObject(url, String.class);*/
        //mailService.sendMail("meritt_j@etna-alternance.net", "Test de mail", "On envoie un mail de test.");
        return ResponseEntity.status(201).body("OUI");
    }

    @PostMapping(value = "/add", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> addVideo(@RequestPart("source") @Valid @NotNull @NotEmpty MultipartFile source, @RequestPart("image") @Valid @NotNull @NotEmpty MultipartFile image, @RequestPart("video") String videoDetails) throws CustomGlobalException, JSONException, IOException {
        VideoDto videoDto = videoService.mapVideoDtoFromJson(videoDetails);
        Video video = videoService.addVideo(videoDto);
        videoService.encodeVideo(source, video.getId().toString(), image);
        videoEncodingService.saveSourceDatabase(video);
        return ResponseEntity.status(201).body(JsonUtils.jsonifyVideo(video).toString());
    }

    @PostMapping(value = "/quality", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> addQuality(@RequestPart("source") @Valid @NotNull @NotEmpty MultipartFile source, @RequestPart("video") String videoDetails) throws CustomGlobalException, JSONException, IOException {
        QualityDto qualityDto = videoService.mapQualityDtoFromJson(videoDetails);
        videoEncodingService.saveQuality(source, qualityDto);
        return ResponseEntity.status(201).body("oui");
    }

    @GetMapping(value = "/get/video/{id}")
    public ResponseEntity<Object> getVideo(@PathVariable(name = "id") Integer id) throws CustomGlobalException, JSONException {
        Video video = videoService.getVideoById(id);
        return ResponseEntity.status(200).headers(headers).body(JsonUtils.jsonifyVideo(video).toString());
    }

    @GetMapping(value = "/get/group/{id}")
    public ResponseEntity<Object> getGroupeById(@PathVariable(name = "id") Integer id) throws CustomGlobalException, JSONException {
        VideoGroup groupe = videoService.getVideoGroupById(id);
        return ResponseEntity.status(200).headers(headers).body(JsonUtils.jsonifyVideoGroup(groupe).toString());
    }

    @PostMapping(value = "/add/group")
    public ResponseEntity<Object> addVideoGroup(@Valid @RequestBody VideoGroupCreationDto videoGroupCreationDto) throws JSONException {
        VideoGroup group = videoService.createVideoGroup(videoGroupCreationDto);
        return ResponseEntity.status(200).headers(headers).body(JsonUtils.jsonifyVideoGroup(group).toString());
    }

    @GetMapping(value = "/all/group")
    public ResponseEntity<Object> getVideoGroup() throws JSONException {
        List<VideoGroup> groupes = videoService.getAllVideoGroup();
        return ResponseEntity.status(200).headers(headers).body(JsonUtils.jsonifyVideoGroupList(groupes).toString());
    }

    @GetMapping(value = "/all/videos/{id}")
    public ResponseEntity<Object> getVideoGroup(@PathVariable(name = "id") Integer id) throws JSONException, CustomGlobalException {
        List<Video> videos = videoService.getAllVideoByGroupId(id);
        return ResponseEntity.status(200).headers(headers).body(JsonUtils.jsonifyVideoList(videos).toString());
    }

    @DeleteMapping(value = "/delete/group/{id}")
    public ResponseEntity<Object> deleteVideoGroup(@PathVariable(name = "id") Integer id) throws CustomGlobalException {
        videoService.deleteVideoGroup(id);
        return ResponseEntity.status(201).headers(headers).body("OK");
    }

    @DeleteMapping(value = "/delete/video/{id}")
    public ResponseEntity<Object> deleteVideo(@PathVariable(name = "id") Integer id) throws CustomGlobalException {
        videoService.deleteVideo(id);
        return ResponseEntity.status(201).headers(headers).body("OK");
    }

    @PutMapping(value = "/modify/group/{id}")
    public ResponseEntity<Object> deleteUpdateGroup(@PathVariable(name = "id") Integer id, @RequestBody VideoGroupCreationDto videoGroupCreationDto) throws CustomGlobalException, JSONException {
        VideoGroup groupe = videoService.modifyGroupe(id, videoGroupCreationDto);
        return ResponseEntity.status(201).headers(headers).body(JsonUtils.jsonifyVideoGroup(groupe).toString());
    }

    @PutMapping(value = "/modify/video/{id}")
    public ResponseEntity<Object> updateVideo(@PathVariable(name = "id") Integer id, @RequestBody VideoDto videoDto) throws CustomGlobalException, JSONException {
        Video video = videoService.updateVideo(id, videoDto);
        return ResponseEntity.status(201).headers(headers).body(JsonUtils.jsonifyVideo(video).toString());
    }

    // FOR FRONT

    @GetMapping(value = "/sort/recent")
    public ResponseEntity<Object> sortAllRecent() throws JSONException {
        List<Video> videos = videoService.sortAllRecent();
        return ResponseEntity.status(200).headers(headers).body(JsonUtils.jsonifyVideoList(videos).toString());
    }

    @GetMapping(value = "/sort/serie/recent")
    public ResponseEntity<Object> sortSerieRecent() throws JSONException {
        List<Video> videos = videoService.sortSerieRecent();
        return ResponseEntity.status(200).headers(headers).body(JsonUtils.jsonifyVideoList(videos).toString());
    }
}
