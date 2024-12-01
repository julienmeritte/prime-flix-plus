package com.etna.primeflixplus.repositories;

import com.etna.primeflixplus.entities.Profile;
import com.etna.primeflixplus.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends CrudRepository<Profile, Integer> {

    List<Profile> getAllByUser(User user);

    Optional<Profile> findById(Integer id);
}
