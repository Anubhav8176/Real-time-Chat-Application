package com.anucodes.chatapplication.services;

import com.anucodes.chatapplication.entities.RefreshToken;
import com.anucodes.chatapplication.entities.UserInfo;
import com.anucodes.chatapplication.repository.RefreshTokenRepository;
import com.anucodes.chatapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServices {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(String username){
        UserInfo userInfoExtracted = userRepository.findByUsername(username);
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userInfoExtracted)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(180000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(!token.getExpiryDate().isAfter(Instant.now())){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken()+" is expired!!");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

}
