package com.gattuso.jwtProject.Service;

import com.gattuso.jwtProject.Model.UserEntity;
import com.gattuso.jwtProject.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;


@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow();
        System.out.println("Repository");
        Collection< ? extends GrantedAuthority> authorities = userEntity.getRoleEntities().stream()
                .map(roleEntity -> new SimpleGrantedAuthority("ROLE_".concat(roleEntity.getRole().name())))
                .toList();
        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }
}
