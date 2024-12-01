package com.etna.primeflixplus.repositories;

import com.etna.primeflixplus.entities.Video;
import com.etna.primeflixplus.entities.VideoGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends CrudRepository<Video, Integer> {
    List<Video> findAllByGroup(VideoGroup groupe);

    Optional<Video> findById(Integer id);

    Page<Video> findAll(Pageable pageable);
}
