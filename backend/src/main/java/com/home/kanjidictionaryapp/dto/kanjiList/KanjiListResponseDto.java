package com.home.kanjidictionaryapp.dto.kanjiList;

import lombok.Value;

import java.util.List;

@Value
public class KanjiListResponseDto {

    Long id;

    String name;

    List<KanjiListElementResponseDto> elements;

}
