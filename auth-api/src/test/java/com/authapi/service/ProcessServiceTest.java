package com.authapi.service;

import com.authapi.dto.process.ProcessRequest;
import com.authapi.dto.process.ProcessResponse;
import com.authapi.model.ProcessingLog;
import com.authapi.model.User;
import com.authapi.repository.ProcessingLogRepository;
import com.authapi.service.impl.ProcessServiceImpl;
import com.authapi.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessServiceTest {

    @Mock
    private WebClient dataWebClient;

    @Mock
    private ProcessingLogRepository processingLogRepository;

    @InjectMocks
    private ProcessServiceImpl processService;

    @Test
    void ProcessText_Success() {
        User user = TestUtil.createUser();
        user.setId(UUID.randomUUID());
        ProcessRequest request = new ProcessRequest("hello");
        String expected = "OLLEH";

        Map<String, String> responseMap = Map.of("text", expected);

        WebClient.RequestBodyUriSpec uriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        ReflectionTestUtils.setField(processService, "internalToken", "secret");

        when(dataWebClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri("/api/transform")).thenReturn(bodySpec);
        when(bodySpec.header("X-Internal-Token", "secret"))
                .thenReturn(bodySpec);
        when(bodySpec.bodyValue(request))
                .thenReturn(headersSpec);
        when(headersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(responseMap));

        ProcessResponse actual = processService.processText(request, user);

        assertEquals(expected, actual.text());
        verify(processingLogRepository).save(any(ProcessingLog.class));
    }

    @Test
    void emptyResponse_ThrowsException() {
        User user = new User();

        ProcessRequest request = new ProcessRequest("hello");

        WebClient.RequestBodyUriSpec uriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        ReflectionTestUtils.setField(processService, "internalToken", "secret");

        when(dataWebClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri("/api/transform")).thenReturn(bodySpec);
        when(bodySpec.header(anyString(), anyString())).thenReturn(bodySpec);
        when(bodySpec.bodyValue(request)).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.empty());

        assertThrows(
                IllegalStateException.class,
                () -> processService.processText(request, user)
        );

        verify(processingLogRepository, never()).save(any());
    }

    @Test
    void badToken_ReturnsForbidden() {
        User user = new User();

        ProcessRequest request = new ProcessRequest("hello");

        WebClient.RequestBodyUriSpec uriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        ReflectionTestUtils.setField(processService, "internalToken", "incorrectkey");

        when(dataWebClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri("/api/transform")).thenReturn(bodySpec);
        when(bodySpec.header(anyString(), anyString())).thenReturn(bodySpec);
        when(bodySpec.bodyValue(request)).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.error(new WebClientResponseException(
                        403, "Forbidden", null, null, null
                )));

        assertThrows(WebClientResponseException.class,
                () -> processService.processText(request, user));

        verify(processingLogRepository, never()).save(any());
    }
}
