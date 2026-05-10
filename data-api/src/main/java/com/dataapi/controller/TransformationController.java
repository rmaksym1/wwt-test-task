package com.dataapi.controller;

import com.dataapi.dto.TransformationRequest;
import com.dataapi.service.TransformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransformationController {
    private final TransformationService transformationService;

    @PostMapping("/transform")
    public ResponseEntity<?> transform(
            @RequestHeader("X-Internal-Token") String actualToken,
            @RequestBody TransformationRequest request
    ) {
        return ResponseEntity.ok(transformationService.transform(request));
    }
}
