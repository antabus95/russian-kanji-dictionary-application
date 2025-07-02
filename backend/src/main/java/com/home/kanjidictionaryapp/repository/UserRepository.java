package com.home.kanjidictionaryapp.repository;

import com.home.kanjidictionaryapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

    Optional<User> findByUsername(String username);

    Page<User> findByUsername(String username, Pageable pageable);

}
