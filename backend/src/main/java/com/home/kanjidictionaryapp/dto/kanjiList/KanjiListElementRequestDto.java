package com.home.kanjidictionaryapp.dto.kanjiList;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class KanjiListElementRequestDto {

    String note;

    Long kanjiId;

    Long kanjiListId;

    LocalDateTime addedAt;

    Boolean isLearned;

}
