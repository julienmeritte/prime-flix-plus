package com.etna.primeflixplus.controllers;

import com.etna.primeflixplus.services.StreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/stream")
public class StreamController {

    private final StreamService streamService;

    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    @GetMapping(value = "/{name}", produces = "video/mp4")
    public Mono<Resource> getVideo(@RequestHeader("Range") String range, @PathVariable("name") String name) {
        return streamService.getVideo(name);
    }

    @GetMapping(value = "/image/{name}", produces = MediaType.IMAGE_PNG_VALUE)
    public Mono<Resource> getImage(@PathVariable("name") String name) {
        return streamService.getImage(name);
    }
}
