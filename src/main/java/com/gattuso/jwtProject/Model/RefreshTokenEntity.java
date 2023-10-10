package com.gattuso.jwtProject.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenEntity {
    private Long Id;
    private String refreshToken;
    private Instant expiryDate;
    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private UserEntity userEntity;
}
