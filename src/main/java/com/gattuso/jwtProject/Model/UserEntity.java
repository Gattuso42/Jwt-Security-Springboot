package com.gattuso.jwtProject.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "userData")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @NotBlank
    @Size(max = 30)
    private String email;
    @NotBlank
    @Size(max = 35)
    private String username;
    @NotBlank
    private String password;

    //    Relationship
    @ManyToMany(fetch = FetchType.EAGER,targetEntity = RoleEntity.class,cascade = CascadeType.PERSIST)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleEntity> roleEntities;

}
