package com.gattuso.jwtProject.Controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleControllerTest {

    @GetMapping("/accessAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accessAdmin(){
        return "Hello,you have accessed as Admin";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/accessUser")
    public String accessUser(){
        return "Hello,you have accessed as User";
    }

    @PreAuthorize("hasRole('INVITED')")
    @GetMapping("/accessInvited")
    public String accessInvited(){
        return "Hello,you have accessed as Invited";
    }
}
