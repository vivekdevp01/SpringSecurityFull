package com.example.productionservices.dtos;

import lombok.*;

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
}
