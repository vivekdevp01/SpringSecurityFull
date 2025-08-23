package com.example.productionservices.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

   @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
       httpSecurity.
               authorizeHttpRequests(auth-> auth
                       .requestMatchers("/api/v1/post").permitAll()
                       .requestMatchers("/api/v1/post/**").hasAnyRole("ADMIN")
                       .anyRequest().authenticated())
               .csrf(AbstractHttpConfigurer::disable)
               .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

//               .formLogin(Customizer.withDefaults())
               ;
       return httpSecurity.build();
   }
 @Bean
  UserDetailsService inMemoryUserDetailsService() {
      UserDetails user=User
              .withUsername("viv")
              .password(passwordEncoder().encode("Abcd1234"))
              .roles("USER")
              .build();
      UserDetails admin=User
              .withUsername("vv")
              .password(passwordEncoder().encode("Abcd1234"))
              .roles("ADMIN")
              .build();
      return new InMemoryUserDetailsManager(user,admin);
  }
  @Bean
  PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
  }

}
