package com.home.kanjidictionaryapp.repository;

import com.home.kanjidictionaryapp.model.Kanji;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KanjiRepository extends JpaRepository<Kanji, Long>, JpaSpecificationExecutor<Kanji> {

    @Query("SELECT k FROM Kanji k WHERE k.radicalForm.radical.id = :radicalId")
    Page<Kanji> findAllByRadicalId(@Param("radicalId") Long radicalId, Pageable pageable);

}
