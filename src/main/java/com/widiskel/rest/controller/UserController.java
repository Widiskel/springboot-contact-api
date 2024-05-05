package com.widiskel.rest.controller;

import com.widiskel.rest.model.ApiRes;
import com.widiskel.rest.model.auth.UserRegisterRequest;
import com.widiskel.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            path = "api/auth/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiRes<String> register(@RequestBody UserRegisterRequest request) {
        userService.register(request);

        return ApiRes.<String>builder().rc("00").msg("User Registered Successfully").build();
    }
}
