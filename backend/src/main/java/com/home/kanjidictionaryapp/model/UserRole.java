package com.home.kanjidictionaryapp.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ADMIN("Администратор"), EDITOR("Редактор"), USER("Пользователь");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
