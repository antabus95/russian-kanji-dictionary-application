package com.home.kanjidictionaryapp.repository;

import com.home.kanjidictionaryapp.model.RadicalForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RadicalFormRepository extends JpaRepository<RadicalForm, Long> {

    @Query(value = "SELECT * FROM radical_forms rf WHERE rf.spelling = :spelling OR rf.alt_spelling = :spelling", nativeQuery = true)
    Optional<RadicalForm> findBySpelling(@Param("spelling") String spelling);

}
