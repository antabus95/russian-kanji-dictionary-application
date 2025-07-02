package com.home.kanjidictionaryapp.dto.radical;

import lombok.Value;

import java.util.List;

@Value
public class ReadUpdateRadicalDto {

    Long id;

    String name;

    Integer number;

    Integer strokeCount;

    String spelling;

    List<ReadUpdateRadicalFormDto> radicalForms;

}
