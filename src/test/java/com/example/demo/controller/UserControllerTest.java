package com.example.demo.controller;

import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired private MockMvc mvc;

    @MockitoBean private UserService userService;

    @Test
    void createUser_returns201_andResponseBody() throws Exception {
        // We mock the service, so controller test doesn't depend on JSON mapper class
        when(userService.create(any())).thenReturn(new com.example.demo.dto.UserDtos.UserResponse(
                1L, "esra", "esra@mail.com"
        ));

        String json = """
                {"username":"esra","email":"esra@mail.com"}
                """;

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("esra"))
                .andExpect(jsonPath("$.email").value("esra@mail.com"));
    }

    @Test
    void createUser_validationFailure_returns400_withViolations() throws Exception {
        String badJson = """
                {"username":"esra","email":""}
                """;

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.violations").isArray());
    }
}
