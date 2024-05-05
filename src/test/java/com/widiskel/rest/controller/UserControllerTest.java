package com.widiskel.rest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.widiskel.rest.entity.User;
import com.widiskel.rest.model.ApiRes;
import com.widiskel.rest.model.auth.*;
import com.widiskel.rest.repository.UserRepository;
import com.widiskel.rest.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        mockMvc.perform(post("/api/auth/register").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpectAll(status().isOk()).andDo(result -> {
            ApiRes<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
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

        mockMvc.perform(post("/api/auth/register").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpectAll(status().isBadRequest()).andDo(result -> {
            ApiRes<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals("Validation Error", response.getMsg());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testRegisterDuplicate() throws Exception {
        generateDummy();

        UserRegisterRequest request = new UserRegisterRequest();
        request.setName("Widiskel");
        request.setUsername("widiskel");
        request.setPassword("12345678");

        mockMvc.perform(post("/api/auth/register").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpectAll(status().isBadRequest()).andDo(result -> {
            ApiRes<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testLoginSuccess() throws Exception {
        generateDummy();

        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("widiskel");
        request.setPassword("12345678");

        mockMvc.perform(post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpectAll(status().isOk()).andDo(result -> {
            ApiRes<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertNotNull(response.getData().getToken());
            assertNotNull(response.getData().getExpiredAt());
        });
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        generateDummy();

        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("widiskeli");
        request.setPassword("12345678");

        mockMvc.perform(post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpectAll(status().isUnauthorized()).andDo(result -> {
            ApiRes<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testLoginUserWrongPassword() throws Exception {
        generateDummy();

        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("widiskel");
        request.setPassword("12348398492");

        mockMvc.perform(post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpectAll(status().isUnauthorized()).andDo(result -> {
            ApiRes<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getUnauthorizedUser() throws Exception {
        mockMvc.perform(get("/api/auth/user").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).header("X-API-TOKEN","notfound")).andExpectAll(status().isUnauthorized()).andDo(result -> {
            ApiRes<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getAuthorizedUser() throws Exception {
        generateDummy();
        mockMvc.perform(get("/api/auth/user").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).header("X-API-TOKEN","lkasjdklajsflkjasf")).andExpectAll(status().isOk()).andDo(result -> {
            ApiRes<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getData());
        });
    }

    @Test
    void updateAuthorizedUser() throws Exception {
        generateDummy();

        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("Widi Saputra");
        request.setPassword("12345678");

        mockMvc.perform(post("/api/auth/user").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)).header("X-API-TOKEN","lkasjdklajsflkjasf")).andExpectAll(status().isOk()).andDo(result -> {
            ApiRes<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getData());
            assertEquals("Widi Saputra",response.getData().getName());
        });
    }

    private void generateDummy() {
        User user = new User();
        user.setName("Widiskel");
        user.setUsername("widiskel");
        user.setToken("lkasjdklajsflkjasf");
        user.setTokenExpiredAt(System.currentTimeMillis()+100000000L);
        user.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));
        userRepository.save(user);
    }

}