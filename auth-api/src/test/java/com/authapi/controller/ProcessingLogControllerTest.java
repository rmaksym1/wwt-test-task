package com.authapi.controller;

import com.authapi.dto.process.ProcessRequest;
import com.authapi.dto.process.ProcessResponse;
import com.authapi.service.impl.ProcessServiceImpl;
import com.authapi.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProcessingLogControllerTest {
    private static final String PROCESS_PATH = "/process";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProcessServiceImpl processService;

    @InjectMocks
    private ProcessController processController;

    @ParameterizedTest
    @DisplayName("Should return output from service B")
    @CsvSource({
            "Anonymous, 403",
            "user@gmail.com, 200"
    })
    void process_ValidRequest_ReturnsValidResponse(String role, int expectedStatus) throws Exception {
        ProcessRequest request = TestUtil.createProcessRequest();

        when(processService.processText(any(), any()))
                .thenReturn(new ProcessResponse("TSET"));

        var requestBuilder = post(PROCESS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        if (!role.equals("Anonymous")) {
            requestBuilder.with(user(role));
        }

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(expectedStatus))
                .andExpect(role.equals("Anonymous")
                        ? status().isForbidden()
                        : jsonPath("$.text").value("TSET"));
    }
}
