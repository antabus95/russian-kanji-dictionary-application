package com.home.kanjidictionaryapp.dto;

import com.home.kanjidictionaryapp.model.UserRole;
import lombok.Value;

@Value
public class ResponseUserDto {

    Long id;

    String username;

    UserRole role;

}
