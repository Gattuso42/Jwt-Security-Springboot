package com.gattuso.jwtProject.Service;


import com.gattuso.jwtProject.Model.RefreshTokenEntity;
import com.gattuso.jwtProject.Repository.RefreshTokenRepository;
import com.gattuso.jwtProject.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;

    public RefreshTokenEntity createRefreshToken(String username){
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .userEntity(userRepository.findByUsername(username).get())
                .refreshToken(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))//10 min
                .build();
        if (isRefreshTokenExist(username)){
            RefreshTokenEntity oldRefreshTokenEntity=userRepository.findByUsername(username).get().getRefreshTokenEntity();
            refreshTokenRepository.delete(oldRefreshTokenEntity);
        }
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public Optional<RefreshTokenEntity>findRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }

    public RefreshTokenEntity verifyExpirationRefreshToken(RefreshTokenEntity refreshToken){
        if(refreshToken.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException(refreshToken.getRefreshToken()+" Refresh token was expired, please make a new sign in request");
        }
        return refreshToken;
    }

    public boolean isRefreshTokenExist(String username){
        return userRepository.findByUsername(username).get().getRefreshTokenEntity().getRefreshToken()!=null;
    }

}
