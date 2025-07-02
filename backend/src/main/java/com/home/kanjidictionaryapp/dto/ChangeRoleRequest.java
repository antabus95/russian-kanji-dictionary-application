package com.home.kanjidictionaryapp.dto;

import com.home.kanjidictionaryapp.model.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import com.home.kanjidictionaryapp.util.EnumValidator;

@Value
public class ChangeRoleRequest {
    @NotNull
    @EnumValidator(enumClass = UserRole.class, message = "Недопустимая роль")
    String role;

}
