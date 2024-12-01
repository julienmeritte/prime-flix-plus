package com.etna.primeflixplus.repositories;

import com.etna.primeflixplus.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByMail(String mail);

    List<User> findAll();

    Optional<User> findUserByVerificationCode(String code);
}
