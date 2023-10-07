package com.gattuso.jwtProject.Controller;


import com.gattuso.jwtProject.Model.RoleEntity;
import com.gattuso.jwtProject.Model.UserDto;
import com.gattuso.jwtProject.Model.UserEntity;
import com.gattuso.jwtProject.Repository.RoleRepository;
import com.gattuso.jwtProject.Repository.UserRepository;
import com.gattuso.jwtProject.Security.ERole;
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

    @DeleteMapping("/delete-user/{id}")
    public String deleteUser (@PathVariable Long id){
        userRepository.deleteById(id);
        return "Object deleted";
    }
}
