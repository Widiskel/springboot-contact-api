package com.widiskel.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.widiskel.rest.entity.User;
import com.widiskel.rest.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.widiskel.rest.model.ApiRes;
import com.widiskel.rest.model.auth.UserRegisterRequest;
import com.widiskel.rest.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setName("Widiskel");
        request.setUsername("widiskel");
        request.setPassword("12345678");

        mockMvc.perform(post("/api/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    ApiRes<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertEquals("User Registered Successfully", response.getMsg());
                    assertEquals("00", response.getRc());
                });
    }

    @Test
    void testRegisterBadRequest() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setName("");
        request.setUsername("");
        request.setPassword("");

        mockMvc.perform(post("/api/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    ApiRes<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                });

                    assertEquals("Validation Error",response.getMsg());
                    assertNotNull(response.getErrors());
                });
    }
@Test
    void testRegisterDuplicate() throws Exception {
        User user = new User();
        user.setName("Widiskel");
        user.setUsername("widiskel");
        user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));
        userRepository.save(user);

        UserRegisterRequest request = new UserRegisterRequest();
        request.setName("Widiskel");
        request.setUsername("widiskel");
        request.setPassword("12345678");

        mockMvc.perform(post("/api/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    ApiRes<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                });

                    assertNotNull(response.getErrors());
                });
    }

}