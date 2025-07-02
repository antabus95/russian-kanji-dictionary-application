package com.home.kanjidictionaryapp.dto.kanjiList;

import com.home.kanjidictionaryapp.dto.kanji.ReadKanjiDto;
import com.home.kanjidictionaryapp.model.Kanji;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class KanjiListElementResponseDto {

    Long id;

    String note;

    LocalDateTime addedAt;

    Boolean isLearned;

    ReadKanjiDto kanji;

}
