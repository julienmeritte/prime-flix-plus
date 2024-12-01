package com.etna.primeflixplus.repositories;

import com.etna.primeflixplus.entities.Profile;
import com.etna.primeflixplus.entities.VideoProfile;
import org.springframework.data.repository.CrudRepository;

public interface VideoProfileRepository extends CrudRepository<VideoProfile, Integer> {
}
