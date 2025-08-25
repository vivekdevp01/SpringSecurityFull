package com.example.productionservices.entities;

import com.example.productionservices.enums.Permission;
import com.example.productionservices.enums.Role;
import com.example.productionservices.utils.PermissionMapping;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   private String name;
    @Column(unique = true)
    private String email;
    private String password;

//    @ElementCollection(fetch = FetchType.EAGER)
//    @Enumerated(EnumType.STRING)
//    private Set<Permission> permissions;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> {
            Set<SimpleGrantedAuthority> permissions = PermissionMapping.getAuthoritiesForRole(role);
            authorities.addAll(permissions);
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
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
