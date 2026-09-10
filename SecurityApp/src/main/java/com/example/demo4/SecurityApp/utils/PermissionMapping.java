package com.example.demo4.SecurityApp.utils;

import com.example.demo4.SecurityApp.entities.enums.Permission;
import com.example.demo4.SecurityApp.entities.enums.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo4.SecurityApp.entities.enums.Permission.*;
import static com.example.demo4.SecurityApp.entities.enums.Role.*;

@Service
public class PermissionMapping {

    private static final Map<Role, Set<Permission>> map = Map.of(

            USER,
            Set.of(
                    USER_VIEW,
                    POST_VIEW
            ),

            CREATOR,
            Set.of(
                    USER_VIEW,
                    POST_VIEW,
                    POST_CREATE,
                    POST_UPDATE
            ),

            ADMIN,
            Set.of(
                    USER_VIEW,
                    USER_CREATE,
                    USER_UPDATE,
                    USER_DELETE,

                    POST_VIEW,
                    POST_CREATE,
                    POST_UPDATE,
                    POST_DELETE
            )
    );

    public static Set<SimpleGrantedAuthority> authorities(Role role) {

        return map.get(role)
                .stream()
                .map(permission ->
                        new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }
}