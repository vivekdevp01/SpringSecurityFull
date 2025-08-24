package com.example.productionservices.controllers;

import com.example.productionservices.advices.ApiResponse;
import com.example.productionservices.dtos.LoginDto;
import com.example.productionservices.dtos.LoginResponseDto;
import com.example.productionservices.dtos.SignupDto;
import com.example.productionservices.dtos.UserDto;
import com.example.productionservices.services.AuthService;
import com.example.productionservices.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService   authService;

    @Value("${deploy.env}")
    private String deployEnv;
    public AuthController(UserService userService,AuthService authService) {
        this.userService = userService;
        this.authService=authService;
    }

    @PostMapping("/signup")
    ResponseEntity<UserDto> signup(@RequestBody SignupDto signupDto) {
        UserDto userDto=userService.signup(signupDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);

    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto,
                                                     HttpServletResponse response) {
        LoginResponseDto loginResponseDto = authService.login(loginDto);

        Cookie cookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponseDto);
    }
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request, HttpServletResponse response) {
     String refreshToken= Arrays.stream(request.getCookies())
              .filter(cookie-> "refreshToken".equals(cookie.getName()))
              .findFirst()
              .map(Cookie::getValue)
              .orElseThrow(()->new AuthenticationServiceException("Refresh token not found inside the cookie"));
     LoginResponseDto loginResponseDto=authService.refreshToken(refreshToken);
     return  ResponseEntity.ok(loginResponseDto);
    }
}
