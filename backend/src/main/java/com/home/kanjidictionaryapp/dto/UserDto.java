package com.home.kanjidictionaryapp.dto;

import com.home.kanjidictionaryapp.model.UserRole;
import lombok.Value;

@Value
public class UserDto {
    Long id;
    String username;
    Boolean enabled;
    String role;
}
