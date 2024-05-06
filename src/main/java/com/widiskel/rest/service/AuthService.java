package com.widiskel.rest.service;

import com.widiskel.rest.entity.User;
import com.widiskel.rest.model.auth.TokenResponse;
import com.widiskel.rest.model.auth.UserLoginRequest;
import com.widiskel.rest.repository.UserRepository;
import com.widiskel.rest.utils.security.BCrypt;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validator;


    @Transactional
    public TokenResponse login(UserLoginRequest request) {
        validator.validate(request);


        User user = userRepository.findById(request.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next30Days());
            userRepository.save(user);

            return TokenResponse.builder().token(user.getToken()).expiredAt(user.getTokenExpiredAt()).build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password");
        }

    }


    private Long next30Days() {
        return System.currentTimeMillis() + (1000 * 16 * 24 * 30);
    }


}
