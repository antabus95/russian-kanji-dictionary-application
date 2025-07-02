package com.home.kanjidictionaryapp.controller;

import com.home.kanjidictionaryapp.dto.ChangeRoleRequest;
import com.home.kanjidictionaryapp.dto.PageResponse;
import com.home.kanjidictionaryapp.dto.UserDto;
import com.home.kanjidictionaryapp.dto.UserRoleDto;
import com.home.kanjidictionaryapp.model.UserRole;
import com.home.kanjidictionaryapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public PageResponse<UserDto> findAll(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 30, sort = "username", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserDto> page;
        if (search != null && !search.isBlank()) {
            page = userService.getByUsername(search, pageable);
        } else {
            page = userService.getAll(pageable);
        }
        return new PageResponse<>(
                page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public UserDto findById(@PathVariable("id") long id) {
        return userService.getById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    @PutMapping("/{id}")
    public UserDto update(@PathVariable("id") long id, @Validated @RequestBody UserDto userDto) {
        return userService.update(id, userDto)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/block")
    @Transactional
    public UserDto blockUser(@PathVariable("id") long id) {
        return userService.setEnabled(id, false)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/unblock")
    @Transactional
    public UserDto unblockUser(@PathVariable("id") long id) {
        return userService.setEnabled(id, true)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/role")
    @Transactional
    public UserDto changeUserRole(
            @PathVariable("id") long id,
            @RequestBody @Valid ChangeRoleRequest request) {
        return userService.updateUserRole(id, request.getRole())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        if(!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserRoleDto> getAllRoles() {
        return Arrays.stream(UserRole.values())
                .map(role -> new UserRoleDto(role.name(), role.getDisplayName()))
                .toList();
    }

}
