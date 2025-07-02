package com.home.kanjidictionaryapp.mapper;

import com.home.kanjidictionaryapp.dto.ResponseUserDto;
import com.home.kanjidictionaryapp.model.User;
import com.home.kanjidictionaryapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseUserMapper implements ReadUpdateMapper<User, ResponseUserDto>{

    @Override
    public User toEntity(ResponseUserDto responseUserDto) {
        User user = new User();
        user.setId(responseUserDto.getId());
        user.setUsername(responseUserDto.getUsername());
        user.setRole(responseUserDto.getRole());
        return user;
    }

    @Override
    public ResponseUserDto toDto(User user) {
        return new ResponseUserDto(user.getId(), user.getUsername(), user.getRole());
    }
}
