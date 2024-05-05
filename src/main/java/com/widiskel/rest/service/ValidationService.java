package com.widiskel.rest.service;

import com.widiskel.rest.model.auth.UserLoginRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class ValidationService {
    @Autowired
    private Validator validator;
    public void validate(Object request){
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            log.error("Validation Error {}", new ConstraintViolationException(constraintViolations).getMessage());

            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
