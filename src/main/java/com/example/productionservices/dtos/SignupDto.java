package com.example.productionservices.dtos;

import com.example.productionservices.enums.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {
//    private long id;
    private String email;
    private String password;
    private String name;
    private Set<Role> roles;
}
