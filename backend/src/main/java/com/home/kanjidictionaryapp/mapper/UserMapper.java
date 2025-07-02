package com.home.kanjidictionaryapp.mapper;

import com.home.kanjidictionaryapp.dto.UserDto;
import com.home.kanjidictionaryapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user){
        return new UserDto(user.getId(), user.getUsername(), user.isEnabled(), user.getRole().getAuthority());
    }

}
