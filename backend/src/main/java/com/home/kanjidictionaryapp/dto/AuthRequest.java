package com.home.kanjidictionaryapp.dto;

import lombok.Value;

@Value
public class AuthRequest {
    String username;
    String password;
}
