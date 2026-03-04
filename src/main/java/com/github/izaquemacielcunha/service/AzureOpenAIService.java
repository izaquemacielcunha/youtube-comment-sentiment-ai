package com.github.izaquemacielcunha.service;

import com.github.izaquemacielcunha.model.azure.AzureOpenAIResponse;
import com.github.izaquemacielcunha.model.azure.request.AzureOpenAIRequest;

public interface AzureOpenAIService {
    AzureOpenAIResponse getVideoSentiment(AzureOpenAIRequest azureOpenAiBodyRequest) throws Exception;
}
