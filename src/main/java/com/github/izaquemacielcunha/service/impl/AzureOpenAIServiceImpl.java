package com.github.izaquemacielcunha.service.impl;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.github.izaquemacielcunha.model.azure.AzureOpenAICredentials;
import com.github.izaquemacielcunha.model.azure.AzureOpenAIResponse;
import com.github.izaquemacielcunha.model.azure.request.AzureOpenAIRequest;
import com.github.izaquemacielcunha.model.azure.response.AzureOpenAIRootResponse;
import com.github.izaquemacielcunha.service.AzureOpenAIService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.json.JsonMapper;

public class AzureOpenAIServiceImpl implements AzureOpenAIService {
    private static final Logger logger = LoggerFactory.getLogger(AzureOpenAIServiceImpl.class);

    private final AzureOpenAICredentials credentials;
    private final HttpClient httpClient;
    private final JsonMapper jsonMapper;

    public AzureOpenAIServiceImpl(AzureOpenAICredentials credentials, HttpClient httpClient, JsonMapper jsonMapper) {
        this.credentials = credentials;
        this.httpClient = httpClient;
        this.jsonMapper = jsonMapper;
    }

    public AzureOpenAIResponse getVideoSentiment(AzureOpenAIRequest azureOpenAiBodyRequest) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(credentials.url() + "?api-version=" + encodeUrlParam(credentials.apiVersion())))
                .timeout(Duration.ofSeconds(20))
                .header("api-key", credentials.apiToken())
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonMapper.toJsonString(azureOpenAiBodyRequest, AzureOpenAIRequest.class), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        //TODO validate response code

        String body = response.body();

        logger.debug("[getVideoSentiment] - Response from azure open ai service: {}", body);

        AzureOpenAIRootResponse rootResponse = jsonMapper.fromJsonString(body, AzureOpenAIRootResponse.class);

        return jsonMapper.fromJsonString(rootResponse.choices().getFirst().message().content(), AzureOpenAIResponse.class);
    }

    private String encodeUrlParam(String param) {
        return URLEncoder.encode(param, StandardCharsets.UTF_8);
    }

}