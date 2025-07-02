package com.home.kanjidictionaryapp.dto.kanjiList;

import lombok.Value;

import java.util.List;

@Value
public class UpdateKanjiListDto {

    Long id;

    String name;

    List<UpdateKanjiListElementDto> elements;

}
