package com.home.kanjidictionaryapp.service.impl;

import com.home.kanjidictionaryapp.dto.RegistrationUserDto;
import com.home.kanjidictionaryapp.dto.ResponseUserDto;
import com.home.kanjidictionaryapp.dto.UserDto;
import com.home.kanjidictionaryapp.mapper.CreateMapper;
import com.home.kanjidictionaryapp.mapper.ReadUpdateMapper;
import com.home.kanjidictionaryapp.mapper.UserMapper;
import com.home.kanjidictionaryapp.model.User;
import com.home.kanjidictionaryapp.model.UserRole;
import com.home.kanjidictionaryapp.repository.UserRepository;
import com.home.kanjidictionaryapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Page<UserDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    @Override
    public Optional<UserDto> getById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    public Page<UserDto> getByUsername(String username, Pageable pageable) {
        return userRepository.findByUsername(username, pageable).map(userMapper::toDto);
    }

    @Transactional
    @Override
    public Optional<UserDto> update(Long id, UserDto userDto) {
        return userRepository.findById(id)
                .map(userRepository::saveAndFlush)
                .map(userMapper::toDto);
    }

    @Transactional
    @Override
    public Optional<UserDto> setEnabled(Long id, Boolean enabled) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEnabled(enabled);
                    return userMapper.toDto(user);
                });
    }

    @Transactional
    @Override
    public Optional<UserDto> updateUserRole(Long id, String role) {
        return userRepository.findById(id).map(user -> {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            user.setRole(userRole);
            return userMapper.toDto(userRepository.save(user));
        });
    }


    @Transactional
    @Override
    public boolean delete(Long id) {
        return userRepository.findById(id)
                .map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush();
                    return true;
                }).orElse(false);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
        Set<GrantedAuthority> authorities = Collections.singleton(user.getRole());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return (User)loadUserByUsername(username);
    }
}
