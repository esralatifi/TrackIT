package com.example.demo.controller;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.service.HabitLogService;
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

@WebMvcTest(HabitLogController.class)
@Import(GlobalExceptionHandler.class)
class HabitLogControllerTest {

    @Autowired private MockMvc mvc;

    @MockitoBean private HabitLogService habitLogService;

    @Test
    void createLog_duplicateDay_returns400() throws Exception {
        when(habitLogService.create(any(), any()))
                .thenThrow(new BadRequestException("Log already exists for habitId=1 and date=2026-01-19"));

        String json = """
                {"date":"2026-01-19","status":"COMPLETED"}
                """;

        mvc.perform(post("/api/habits/1/logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Log already exists for habitId=1 and date=2026-01-19"));
    }
}
