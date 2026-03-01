package com.manoj.hireflow.dto;

import com.manoj.hireflow.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
}
