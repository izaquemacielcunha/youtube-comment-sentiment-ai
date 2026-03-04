package com.github.izaquemacielcunha.model.azure;

public record AzureOpenAiCredentials(
        String url,
        String apiVersion,
        String apiToken
) { }
