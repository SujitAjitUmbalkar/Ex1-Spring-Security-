package com.example.demo4.SecurityApp.services;

import com.example.demo4.SecurityApp.entities.Session;
import com.example.demo4.SecurityApp.entities.UserEntity;
import com.example.demo4.SecurityApp.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService
{
    private final SessionRepository sessionRepository;
    private final int SESSION_LIMIT = 2;

    public void generateNewSession(UserEntity user , String refreshToken)
    {
        List<Session> sessions = sessionRepository.findByUser(user);            // get sessions of user

        if(sessions.size() == SESSION_LIMIT)                                                                            // validate size
        {
            sessions.sort(Comparator.comparing(Session::getLastUsedAt));        // sort in asc

            Session lastUsedSession = sessions.getFirst();                  // get last used

            sessionRepository.delete(lastUsedSession);              // delete last session from repo
        }

//        create new session obj
        Session newSession = Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();

//        save session
        sessionRepository.save(newSession);
    }

    public void validSession(String refreshToken)
    {
        Session session =  sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()-> new SessionAuthenticationException("Session Not Found for this Refresh Token "+refreshToken));

        session.setLastUsedAt(LocalDateTime.now());

        sessionRepository.save(session);
    }


}
