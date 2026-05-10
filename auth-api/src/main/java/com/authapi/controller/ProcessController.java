package com.authapi.controller;

import com.authapi.dto.process.ProcessRequest;
import com.authapi.model.User;
import com.authapi.service.ProcessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProcessController {
    private final ProcessService processService;

    @PostMapping("/process")
    public ResponseEntity<?> process(
            @RequestBody @Valid ProcessRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(processService.processText(request, user));
    }
}
