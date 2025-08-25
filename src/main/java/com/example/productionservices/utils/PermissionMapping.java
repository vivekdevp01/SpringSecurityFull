package com.example.productionservices.utils;

import com.example.productionservices.enums.*;
import com.example.productionservices.enums.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.productionservices.enums.Permission.*;
import static com.example.productionservices.enums.Role.*;

public class PermissionMapping {
  private static final Map<Role, Set<Permission>> map=Map.of(
            USER,Set.of(USER_VIEW,POST_VIEW),
            CREATOR,Set.of(USER_UPDATE,POST_UPDATE,POST_CREATE),
            ADMIN,Set.of(USER_CREATE,USER_DELETE,POST_DELETE,USER_UPDATE,POST_UPDATE,POST_CREATE)
    );

    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Role role){
        return map.get(role).stream()
                .map(permission-> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }
}
