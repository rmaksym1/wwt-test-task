package com.origin.service.impl;

import com.origin.dto.TransformationRequest;
import com.origin.dto.TransformationResponse;
import com.origin.service.TransformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransformationServiceImpl implements TransformationService {

    @Override
    public TransformationResponse transform(TransformationRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.text().toUpperCase());
        String outputText = sb.reverse().toString();
        return new TransformationResponse(outputText);
    }
}
