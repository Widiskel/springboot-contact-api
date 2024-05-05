package com.widiskel.rest.service;

import com.widiskel.rest.entity.User;
import com.widiskel.rest.model.auth.UserRegisterRequest;
import com.widiskel.rest.repository.UserRepository;
import com.widiskel.rest.security.BCrypt;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    @Transactional
    public void register(UserRegisterRequest request){
        Set<ConstraintViolation<UserRegisterRequest>> constraintViolations = validator.validate(request);
        if(!constraintViolations.isEmpty()){
            log.error("Validation Error {}", new ConstraintViolationException(constraintViolations).getMessage());

            throw new ConstraintViolationException(constraintViolations);
        }

        if(userRepository.existsById(request.getUsername())){
            log.error("Bad Request Error {}", new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered").getReason());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username already registered");
        }


        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(),BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);
    }
}
