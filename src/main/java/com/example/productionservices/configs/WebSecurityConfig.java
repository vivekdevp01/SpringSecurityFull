package com.example.productionservices.configs;

import com.example.productionservices.filters.JwtAuthFilter;
import com.example.productionservices.handlers.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
   private final JwtAuthFilter jwtAuthFilter;
   private final OAuth2SuccessHandler oAuth2SuccessHandler;
   @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
       httpSecurity.
               authorizeHttpRequests(auth-> auth
                       .requestMatchers("/home.html","/api/v1/auth","/api/v1/auth/**","/login/oauth2/**").permitAll()
                       .anyRequest().authenticated())
               .csrf(AbstractHttpConfigurer::disable)
               .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
               .oauth2Login(oauth2Config-> oauth2Config
                       .successHandler(oAuth2SuccessHandler))


//               .formLogin(Customizer.withDefaults())
               ;
       return httpSecurity.build();
   }
// @Bean
//  UserDetailsService inMemoryUserDetailsService() {
//      UserDetails user=User
//              .withUsername("viv")
//              .password(passwordEncoder().encode("Abcd1234"))
//              .roles("USER")
//              .build();
//      UserDetails admin=User
//              .withUsername("vv")
//              .password(passwordEncoder().encode("Abcd1234"))
//              .roles("ADMIN")
//              .build();
//      return new InMemoryUserDetailsManager(user,admin);
//  }
    @Bean
    AuthenticationManager  authenticationManager(AuthenticationConfiguration config) throws Exception {
       return config.getAuthenticationManager();
    }


}
