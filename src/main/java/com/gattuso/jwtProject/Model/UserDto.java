package com.gattuso.jwtProject.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Email
    @NotBlank
    @Size(max = 30)
    private String email;
    @NotBlank
    @Size(max = 35)
    private String username;
    @NotBlank
    private String password;
    List<String>roles;
}
