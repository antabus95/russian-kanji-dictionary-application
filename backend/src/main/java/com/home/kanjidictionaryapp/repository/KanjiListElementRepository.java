package com.home.kanjidictionaryapp.repository;

import com.home.kanjidictionaryapp.model.KanjiListElement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KanjiListElementRepository extends JpaRepository<KanjiListElement, Long> {

}
