package com.home.kanjidictionaryapp.repository;

import com.home.kanjidictionaryapp.model.KanjiList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KanjiListRepository extends JpaRepository<KanjiList, Long> {


    Page<KanjiList> findAllByUserId(Long userId, Pageable pageable);

    Optional<KanjiList> findByUserIdAndName(Long userId, String name);

    Optional<KanjiList> findByIdAndUserId(Long id, Long userId);

    @Query(value = """
        SELECT kl
        FROM KanjiList kl
        JOIN kl.elements e
        WHERE lower(e.kanji.spelling) = lower(:spelling)
                AND kl.user.id = :userId
        """)
    Page<KanjiList> findAllByUserIdAndKanjiSpelling(Long userId, String spelling, Pageable pageable);


}
