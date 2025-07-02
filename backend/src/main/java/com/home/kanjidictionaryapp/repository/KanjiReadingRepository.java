package com.home.kanjidictionaryapp.repository;

import com.home.kanjidictionaryapp.model.KanjiReading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KanjiReadingRepository extends JpaRepository<KanjiReading, Long> {
}
