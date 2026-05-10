package com.authapi.service;

import com.authapi.dto.process.ProcessRequest;
import com.authapi.dto.process.ProcessResponse;
import com.authapi.model.User;

public interface ProcessService {
    ProcessResponse processText(ProcessRequest request, User user);
}
