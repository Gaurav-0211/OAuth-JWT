package com.oauth.dto;

import com.oauth.config.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class LoginDto {
    private String email;
    private String password;
    private RoleType role;
}
