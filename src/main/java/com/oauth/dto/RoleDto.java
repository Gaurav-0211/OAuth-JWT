package com.oauth.dto;

import com.oauth.config.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleDto {
    private int id;
    private RoleType name;

}
