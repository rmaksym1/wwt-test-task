package com.authapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${data.api.url}")
    private String dataApiUrl;

    @Bean
    public WebClient dataWebClient() {
        return WebClient.builder()
                .baseUrl(dataApiUrl)
                .build();
    }
}
