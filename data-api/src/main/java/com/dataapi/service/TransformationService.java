package com.dataapi.service;

import com.dataapi.dto.TransformationRequest;
import com.dataapi.dto.TransformationResponse;

public interface TransformationService {
    TransformationResponse transform(TransformationRequest request);
}
