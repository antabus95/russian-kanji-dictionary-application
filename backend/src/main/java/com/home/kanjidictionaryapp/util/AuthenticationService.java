package com.home.kanjidictionaryapp.util;

import com.home.kanjidictionaryapp.dto.AuthRequest;
import com.home.kanjidictionaryapp.dto.AuthResponse;
import com.home.kanjidictionaryapp.model.User;
import com.home.kanjidictionaryapp.model.UserRole;
import com.home.kanjidictionaryapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;


    public AuthResponse register(AuthRequest authRequest) {

        if (userRepository.findByUsername(authRequest.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь с таким логином уже существует");
        }

        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        userRepository.save(user);

        String jwt = jwtTokenUtil.generateToken(user);

        return new AuthResponse(jwt);

    }


    public AuthResponse authenticate(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный логин или пароль");
        }


        User user = userRepository.findByUsername(authRequest.getUsername()).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не найден"));

        String jwtToken = jwtTokenUtil.generateToken(user);

        return new AuthResponse(jwtToken);

    }

}
