package com.example.demo4.SecurityApp.entities;


import com.example.demo4.SecurityApp.entities.enums.Role;
import com.example.demo4.SecurityApp.utils.PermissionMapping;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserEntity implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String name;


    @ElementCollection(fetch = FetchType.EAGER )      // Store multiple roles in a separate table and load them immediately.
    @Enumerated(EnumType.STRING)                     // Save enum values as strings (e.g., ADMIN, USER) instead of numbers.
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role_name")
    private Set<Role> roles;

//    you can store permissions in db  , we are using another way

//    @ElementCollection(fetch = FetchType.EAGER )
//    @Enumerated(EnumType.STRING)
//    private Set<Permission> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        Set<GrantedAuthority> authorities = new HashSet<>();

        roles.forEach(role -> {
                            // Add permissions of this role
                            Set<SimpleGrantedAuthority> permissions =  PermissionMapping.authorities(role);

                            authorities.addAll(permissions);

                            // Add role authority
                            authorities.add(
                                    new SimpleGrantedAuthority("ROLE_" + role.name())
            );
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
