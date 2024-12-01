package com.etna.primeflixplus.services;

import com.etna.primeflixplus.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class StreamService {

    private final ResourceLoader resourceLoader;

    private final Environment env;


    public StreamService(ResourceLoader resourceLoader, Environment env) {
        this.resourceLoader = resourceLoader;
        this.env = env;
    }

    public Mono<Resource> getVideo(String name) {
        return Mono.fromSupplier(() -> this.resourceLoader.getResource(Constants.STATIC_VIDEO_PATH + env.getProperty("primeflix.static") + name));
    }

    public Mono<Resource> getImage(String name) {
        return Mono.fromSupplier(() -> this.resourceLoader.getResource(Constants.STATIC_VIDEO_PATH + env.getProperty("primeflix.images") + name + ".png"));
    }
}
