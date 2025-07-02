package com.home.kanjidictionaryapp.dto.radical;

import lombok.Value;

import java.util.List;

@Value
public class CreateRadicalDto {

    String name;

    Integer number;

    Integer strokeCount;

    String spelling;

    List<CreateRadicalFormDto> radicalForms;

}
