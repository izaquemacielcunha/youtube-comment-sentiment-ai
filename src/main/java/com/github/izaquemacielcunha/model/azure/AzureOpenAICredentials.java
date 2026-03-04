package com.github.izaquemacielcunha.model.azure;

public record AzureOpenAICredentials(
        String url,
        String apiToken,
        String apiVersion
) { }