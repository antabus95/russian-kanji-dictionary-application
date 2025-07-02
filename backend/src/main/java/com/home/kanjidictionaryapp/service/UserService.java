package com.home.kanjidictionaryapp.service;

import com.home.kanjidictionaryapp.dto.UserDto;
import com.home.kanjidictionaryapp.model.User;
import com.home.kanjidictionaryapp.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    Page<UserDto> getAll(Pageable pageable);

    Optional<UserDto> getById(Long id);

    Page<UserDto> getByUsername(String username, Pageable pageable);

    Optional<UserDto> update(Long id, UserDto userDto);

    Optional<UserDto> setEnabled(Long id, Boolean enabled);

    Optional<UserDto> updateUserRole(Long id, String role);

    boolean delete(Long id);

    UserDetails loadUserByUsername(String username);

    User getCurrentUser();
}
