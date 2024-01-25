package com.example.api_server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.api_server.filter.AuthFilter;
import com.example.api_server.model.Auth;
import com.example.api_server.model.User;
import com.example.api_server.service.*;
import com.example.api_server.persistence.*;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @PostMapping("/signup")
    public User create(
        @RequestBody 
        @JsonView(value=UserMethod.Post.class) 
        @Validated(UserMethod.Post.class)
        User user) throws Exception{
        return this.userService.create(user);
    }

    @PostMapping("/signin")
    @JsonView(value=UserMethod.Token.class)
    public Auth token(
        @RequestBody 
        @JsonView(value=UserMethod.SignIn.class)
        Auth body){
            return authService.token(body);
    }
    // use for admin role
    //********************/
    // @PutMapping("/users/{userId}")
    // public User update(
    //     @PathVariable(required=true,name="userId") 
    //     Integer userId, 
    //     @RequestBody 
    //     @JsonView(value=UserMethod.Put.class) 
    //     @Validated(UserMethod.Put.class)
    //     User user){
    //     return this.userService.update(userId, user);
    // }

    @PutMapping("/users/info")
    public User update(
        Authentication authentication,
        @RequestBody 
        @JsonView(value=UserMethod.Put.class) 
        @Validated(UserMethod.Put.class)
        User user){
            User loggedInUser = (User) authentication.getPrincipal();
        return this.userService.update(loggedInUser.getId(), user);
    }

    @GetMapping("/users/info")
    @JsonView(value = UserScope.UserDetail.class) 
    public User getUser(
        Authentication authentication,
        @RequestBody 
        @JsonView(value=UserMethod.Put.class) 
        @Validated(UserMethod.Put.class)
        User user){
            User loggedInUser = (User) authentication.getPrincipal();
        return loggedInUser;
    }

    @GetMapping("/users")
    @JsonView(value = UserScope.UserSummary.class) 
    public Iterable<User> getUsers(){
        logger.info("Im here");
        return this.userService.getUsers();
    }
}
