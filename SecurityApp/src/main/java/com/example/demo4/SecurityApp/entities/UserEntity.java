package com.example.demo4.SecurityApp.entities;

import com.example.demo4.SecurityApp.entities.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

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

    @ElementCollection(fetch = FetchType.EAGER)      // Store multiple roles in a separate table and load them immediately.
    @CollectionTable(               //Tells Hibernate to create a separate join table named "user_roles"
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id") // * linked to the main user table using the foreign key column "user_id".
    )
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)                     // Save enum values as strings (e.g., ADMIN, USER) instead of numbers.
    private Set<Role> roles;                         // A user can have multiple roles.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return roles.stream()                        // Iterate over all roles.
                .map(role ->                         // Convert each Role enum...
                        new SimpleGrantedAuthority("ROLE_" + role.name())) // ...into Spring Security authority (ROLE_ADMIN).
                .collect(Collectors.toSet());        // Return all authorities as a Set.
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

/*

    @CollectionTable(               //Tells Hibernate to create a separate join table named "user_roles"
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id") // * linked to the main user table using the foreign key column "user_id".
    )
    @Column(name = "role_name")

, Hibernate can guess the wrong default table names or fail to find old data.
These lines force Hibernate to look in the exact same table (user_roles) and column every single time.

 */