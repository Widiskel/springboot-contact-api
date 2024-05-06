package com.widiskel.rest.controller;

import com.widiskel.rest.entity.User;
import com.widiskel.rest.model.ApiRes;
import com.widiskel.rest.model.auth.*;
import com.widiskel.rest.service.AuthService;
import com.widiskel.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping(
            path = "api/auth/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiRes<String> register(@RequestBody UserRegisterRequest request) {
        userService.register(request);

        return ApiRes.<String>builder().rc("00").msg("User Registered Successfully").build();
    }

    @PostMapping(
            path = "api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiRes<TokenResponse> login(@RequestBody UserLoginRequest request) {
        TokenResponse res = authService.login(request);
        return ApiRes.<TokenResponse>builder().rc("00").msg("Login Success").data(res).build();
    }

    @GetMapping(
            path = "api/auth/user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiRes<UserResponse> user(User user) {
        UserResponse res = userService.getUser(user);
        return ApiRes.<UserResponse>builder().rc("00").msg("Success").data(res).build();
    }

    @PostMapping(
            path = "api/auth/user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiRes<UserResponse> login(User user,@RequestBody UserUpdateRequest request) {
        UserResponse res = userService.updateUser(user,request);
        return ApiRes.<UserResponse>builder().rc("00").msg("Success").data(res).build();
    }

    @DeleteMapping(
            path = "api/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiRes<String> logout(User user) {
        userService.logout(user);
        return ApiRes.<String>builder().rc("00").msg("Success").data("Successfully Logout").build();
    }
}
