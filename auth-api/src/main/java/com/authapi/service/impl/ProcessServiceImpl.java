package com.authapi.service.impl;

import com.authapi.dto.process.ProcessRequest;
import com.authapi.dto.process.ProcessResponse;
import com.authapi.model.ProcessingLog;
import com.authapi.model.User;
import com.authapi.repository.ProcessingLogRepository;
import com.authapi.service.ProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {
    private final WebClient dataWebClient;
    private final ProcessingLogRepository processingLogRepository;

    @Value("${internal.token}")
    private String internalToken;

    @Override
    public ProcessResponse processText(ProcessRequest request, User user) {
        Map<String, String> response = dataWebClient.post()
                .uri("/api/transform")
                .header("X-Internal-Token", internalToken)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

        String text = (response != null) ? response.get("text") : null;

        if (text == null) {
            throw new IllegalStateException("Empty response from data-api");
        }

        ProcessingLog processingLog = new ProcessingLog();
        processingLog.setUser(user);
        processingLog.setInputText(request.text());
        processingLog.setOutputText(text);
        processingLog.setCreatedAt(LocalDateTime.now());

        processingLogRepository.save(processingLog);

        return new ProcessResponse(text);
    }
}
