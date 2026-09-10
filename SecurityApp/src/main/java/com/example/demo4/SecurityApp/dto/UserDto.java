package com.example.demo4.SecurityApp.dto;

import com.example.demo4.SecurityApp.entities.enums.Permission;
import com.example.demo4.SecurityApp.entities.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto
{
    private Long id;
    private String email;
    private String name;
    private Set<Role> roles;
    private Set<Permission> permissions;
}
