package com.home.kanjidictionaryapp.dto.radical;

import lombok.Value;

@Value
public class ReadUpdateRadicalFormDto {

    Long id;

    String formName;

    String spelling;

    String altSpelling;

    Boolean _deleted;

}
