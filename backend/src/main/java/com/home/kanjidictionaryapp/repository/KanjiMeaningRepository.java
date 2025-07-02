package com.home.kanjidictionaryapp.repository;

import com.home.kanjidictionaryapp.model.KanjiMeaning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KanjiMeaningRepository extends JpaRepository<KanjiMeaning, Long> {
}
