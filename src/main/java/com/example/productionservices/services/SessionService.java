package com.example.productionservices.services;

import com.example.productionservices.entities.Session;
import com.example.productionservices.entities.User;
import com.example.productionservices.repositories.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final int SESSION_LIMIT=3;
    public void generateNewSession(User user,String refreshToken) {
        List<Session> userSessions = sessionRepository.findByUser(user);
        if (userSessions.size() == SESSION_LIMIT) {
            userSessions.sort(Comparator.comparing(Session::getLastUsedAt));

            Session oldestSession = userSessions.getFirst();
            sessionRepository.delete(oldestSession);

        }
        Session newSession = Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        sessionRepository.save(newSession);
    }
    public void isRefreshTokenValid(String refreshToken) {
        Session session= sessionRepository.findByRefreshToken(refreshToken).orElseThrow(()->new SessionAuthenticationException("session not found for this refresh token"+refreshToken));
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(session);

    }
    public void logout(String refreshToken) {
        Session session= sessionRepository.findByRefreshToken(refreshToken).orElseThrow(()->new SessionAuthenticationException("session not found for this refresh token"+refreshToken));
        sessionRepository.delete(session);
    }
    @Transactional
    public void logoutAllSessions(User user) {
        List<Session> userSessions = sessionRepository.findByUser(user);
        if(userSessions.isEmpty()){
            throw new SessionAuthenticationException("session not found"+user.getId());
        }
        sessionRepository.deleteAll(userSessions);
    }
}
