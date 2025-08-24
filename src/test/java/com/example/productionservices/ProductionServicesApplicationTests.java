package com.example.productionservices;

import com.example.productionservices.entities.User;
import com.example.productionservices.repositories.UserRepository;
import com.example.productionservices.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductionServicesApplicationTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @Test
    void contextLoads() {

        User user = new User(3L,"viv@gmail.com","Abcd1234");
//        userRepository.save(user);
        String token=jwtService.generateToken(user);
        System.out.println(token);

        Long id=jwtService.getUserIdFromJwtToken(token);
        System.out.println(id);
    }

}
