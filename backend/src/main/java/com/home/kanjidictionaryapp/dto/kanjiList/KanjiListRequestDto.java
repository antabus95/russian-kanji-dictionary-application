package com.home.kanjidictionaryapp.dto.kanjiList;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.util.List;

@Value
public class KanjiListRequestDto {

    @NotBlank
    String name;

    List<KanjiListElementRequestDto> elements;

}
