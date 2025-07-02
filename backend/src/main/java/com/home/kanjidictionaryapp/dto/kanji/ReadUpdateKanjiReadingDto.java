package com.home.kanjidictionaryapp.dto.kanji;

import com.home.kanjidictionaryapp.model.ChineseReadingCategory;
import com.home.kanjidictionaryapp.model.ReadingType;
import lombok.Value;

@Value
public class ReadUpdateKanjiReadingDto {

    Long id;

    String text;

    ReadingType readingType;

    ChineseReadingCategory chineseReadingCategory;

    boolean joyo;

}
