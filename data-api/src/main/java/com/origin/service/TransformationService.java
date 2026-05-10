package com.origin.service;

import com.origin.dto.TransformationRequest;
import com.origin.dto.TransformationResponse;

public interface TransformationService {
    TransformationResponse transform(TransformationRequest request);
}
