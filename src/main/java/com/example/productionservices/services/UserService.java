package com.example.productionservices.services;

import com.example.productionservices.dtos.SignupDto;
import com.example.productionservices.dtos.UserDto;
import com.example.productionservices.entities.User;
import com.example.productionservices.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
//@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager = null;
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;
   private final JwtService jwtService;
//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManagerBuilder authenticationManagerBuilder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.modelMapper = new ModelMapper();
//        this.authenticationManager=uthenticationManager;
//        this.jwtService=jwtService;
//
//    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User with email"+username+"not found"));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("user not found"+userId));

    }
    public UserDto signup(SignupDto signupDto) {
        Optional<User> user=userRepository.findByEmail(signupDto.getEmail());
        if(user.isPresent()){
            throw new BadCredentialsException("User already exists"+signupDto.getEmail());
        }
        User toCreate=modelMapper.map(signupDto,User.class);
        toCreate.setPassword(passwordEncoder.encode(toCreate.getPassword()));
        User savedUser=userRepository.save(toCreate);
        return modelMapper.map(savedUser,UserDto.class);
    }


}
