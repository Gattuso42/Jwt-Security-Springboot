package com.gattuso.jwtProject.Controller;


import com.gattuso.jwtProject.Model.*;
import com.gattuso.jwtProject.Repository.RoleRepository;
import com.gattuso.jwtProject.Repository.UserRepository;
import com.gattuso.jwtProject.Security.ERole;
import com.gattuso.jwtProject.Security.JwtUtils;
import com.gattuso.jwtProject.Service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping()
@AllArgsConstructor
public class UserController {


    private UserRepository userRepository;
    private PasswordEncoder encoder ;
    private JwtUtils jwtUtils;
    private RefreshTokenService refreshTokenService;


    @GetMapping("/hello")
    public String getHello(){
        return "hello not secured";
    }
    @GetMapping("/hello-secured")
    public String getHelloSecured(){
        return "hello secured";
    }

    @PostMapping("/create-user")
    public ResponseEntity<?>createUser(@Valid @RequestBody UserDto user){
        List<RoleEntity>roles = user.getRoles().stream()
                .map(role -> RoleEntity.builder()
                        .role(ERole.valueOf(role))
                        .build()
                ).toList();
        UserEntity userEntity = UserEntity.builder()
                .username(user.getUsername())
                .password(encoder.encode(user.getPassword()))
                .email(user.getEmail())
                .roleEntities(roles)
                .build();

        userRepository.save(userEntity);
        return new ResponseEntity<>(userEntity,HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public JwtResponseDto refreshTokenResponse(@RequestBody JwtRequestDto jwtRequestDto){
       return refreshTokenService.findRefreshToken(jwtRequestDto.getRefreshToken())
                .map(refreshTokenService::verifyExpirationRefreshToken)
                .map(RefreshTokenEntity::getUserEntity)
                .map(userEntity -> {
                    String newAccessToken = jwtUtils.generateAccessToken(userEntity.getUsername());
                    return JwtResponseDto.builder()
                            .refreshToken(jwtRequestDto.getRefreshToken())
                            .accessToken(newAccessToken)
                            .build();
                }).orElseThrow(()->new RuntimeException("Refresh token is not in the database"));
    }

    @DeleteMapping("/delete-user/{id}")
    public String deleteUser (@PathVariable Long id){
        userRepository.deleteById(id);
        return "Object deleted";
    }
}
