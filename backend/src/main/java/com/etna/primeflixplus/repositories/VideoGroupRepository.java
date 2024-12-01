package com.etna.primeflixplus.repositories;

import com.etna.primeflixplus.entities.User;
import com.etna.primeflixplus.entities.Video;
import com.etna.primeflixplus.entities.VideoGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VideoGroupRepository  extends CrudRepository<VideoGroup, Integer> {
    Optional<VideoGroup> findById(Integer id);
    List<VideoGroup> findAll();
}
