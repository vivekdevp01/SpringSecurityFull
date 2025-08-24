package com.example.productionservices.handlers;

import com.example.productionservices.entities.User;
import com.example.productionservices.services.JwtService;
import com.example.productionservices.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User ;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${deploy.env}")
    private String deployEnv;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        log.info("➡️ Entered onAuthenticationSuccess");

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        log.info("✅ OAuth2AuthenticationToken received: {}", token);

        DefaultOAuth2User  oAuth2User  = (DefaultOAuth2User ) token.getPrincipal();
        log.info("✅ Principal extracted: {}", oAuth2User );

        String email = oAuth2User .getAttribute("email");
        String name = oAuth2User .getAttribute("name");

        log.info("📧 Extracted email: {}", email);
        log.info("👤 Extracted name: {}", name);

        User user = userService.getUserByEmail(email);

        if (user == null) {
            log.info("⚠️ No user found in DB, creating new user for email {}", email);

            user = User.builder()
                    .name(name)
                    .email(email)
                    .password(passwordEncoder.encode("OAUTH2_USER")) // Store a dummy password
                    .build();

            try {
                user = userService.save(user);
                System.out.println("saved"+user);
                log.info("✅ New OAuth2 user successfully saved: {}", user);
            } catch (Exception e) {
                log.error("❌ Failed to save new user in DB for email {}. Error: {}", email, e.getMessage(), e);
                throw new ServletException("User  could not be saved to DB", e);
            }
        } else {
            log.info("✅ Existing user found in DB: {}", user);
        }

        // Generate JWTs
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        log.info("🔑 Generated JWT tokens for {}", email);

        // Add refresh token cookie
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equalsIgnoreCase(deployEnv));
        cookie.setPath("/");
        response.addCookie(cookie);
        log.info("🍪 Refresh token cookie added for {}", email);

        // Redirect to frontend with access token
        String frontEndUrl = "http://localhost:8300/home.html?token=" + accessToken;
        log.info("➡️ Redirecting to frontend: {}", frontEndUrl);

//        getRedirectStrategy().sendRedirect(request, response, frontEndUrl);
        response.sendRedirect(frontEndUrl);
    }
}
