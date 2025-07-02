package com.home.kanjidictionaryapp.mapper;

import com.home.kanjidictionaryapp.dto.RegistrationUserDto;
import com.home.kanjidictionaryapp.model.User;
import com.home.kanjidictionaryapp.model.UserRole;
import org.springframework.stereotype.Component;

@Component
public class RegistrationUserMapper implements CreateMapper<User, RegistrationUserDto> {

    @Override
    public User toEntity(RegistrationUserDto registrationUserDto) {
        User user = new User();
        user.setUsername(registrationUserDto.getUsername());
        user.setPassword(registrationUserDto.getPassword());
        user.setEnabled(true);
        user.setRole(UserRole.USER);
        return user;
    }

    @Override
    public RegistrationUserDto toDto(User user) {
        return new RegistrationUserDto(user.getUsername(), user.getPassword());
    }


}
