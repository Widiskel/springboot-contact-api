package com.widiskel.rest.service;

import com.widiskel.rest.entity.User;
import com.widiskel.rest.model.auth.UserRegisterRequest;
import com.widiskel.rest.model.auth.UserResponse;
import com.widiskel.rest.model.auth.UserUpdateRequest;
import com.widiskel.rest.repository.UserRepository;
import com.widiskel.rest.security.BCrypt;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validator;

    @Transactional
    public void register(UserRegisterRequest request) {
        validator.validate(request);

        if (userRepository.existsById(request.getUsername())) {
            log.error("Bad Request Error {}", new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered").getReason());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
        }


        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);
    }

    public UserResponse getUser(User user) {
        return UserResponse.builder().name(user.getName()).username(user.getUsername()).build();
    }


    public UserResponse updateUser(User user,UserUpdateRequest request) {
        validator.validate(request);

        if(Objects.nonNull(request.getName())){
            user.setName(request.getName());
        }
        if(Objects.nonNull(request.getPassword())){
            user.setPassword(BCrypt.hashpw(request.getPassword(),BCrypt.gensalt()));
        }
        userRepository.save(user);

        return UserResponse.builder().name(user.getName()).username(user.getUsername()).build();
    }
}
