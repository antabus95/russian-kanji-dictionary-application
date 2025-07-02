package com.home.kanjidictionaryapp.dto.kanjiList;

import lombok.Value;

@Value
public class UpdateKanjiListElementDto {

    Long id;

    String note;

    Long kanjiId;

}
