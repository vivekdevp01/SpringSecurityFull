package com.example.productionservices.controllers;

import com.example.productionservices.dtos.LoginDto;
import com.example.productionservices.dtos.SignupDto;
import com.example.productionservices.dtos.UserDto;
import com.example.productionservices.services.AuthService;
import com.example.productionservices.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService   authService;
    public AuthController(UserService userService,AuthService authService) {
        this.userService = userService;
        this.authService=authService;
    }

    @PostMapping("/signup")
    ResponseEntity<UserDto> signup(@RequestBody SignupDto signupDto) {
        UserDto userDto=userService.signup(signupDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }
    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody LoginDto  loginDto, HttpServletRequest  request, HttpServletResponse  response ) {
        String token=authService.login(loginDto);
        Cookie cookie=new Cookie("token",token);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
