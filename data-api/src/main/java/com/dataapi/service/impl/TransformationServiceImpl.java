package com.dataapi.service.impl;

import com.dataapi.dto.TransformationRequest;
import com.dataapi.dto.TransformationResponse;
import com.dataapi.service.TransformationService;
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
