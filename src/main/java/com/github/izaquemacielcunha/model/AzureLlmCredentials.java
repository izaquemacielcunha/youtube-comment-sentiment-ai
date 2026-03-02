package com.github.izaquemacielcunha.model;

public record AzureLlmCredentials(
        String url,
        String token
) {
    static {
        System.getenv("AZURE_LLM_API_URL");
        System.getenv("AZURE_LLM_API_KEY");
    }
}
