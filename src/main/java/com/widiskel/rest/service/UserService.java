package com.widiskel.rest.service;

import com.widiskel.rest.entity.User;
import com.widiskel.rest.model.auth.UserRegisterRequest;
import com.widiskel.rest.repository.UserRepository;
import com.widiskel.rest.security.BCrypt;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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


}
