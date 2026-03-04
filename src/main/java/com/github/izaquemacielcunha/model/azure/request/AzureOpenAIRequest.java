package com.github.izaquemacielcunha.model.azure.request;

import java.util.List;

public record AzureOpenAIRequest(
        List<Message> messages,
        int temperature,
        double top_p,
        int max_tokens
) {
    public AzureOpenAIRequest(List<Message> messages) {
        this(messages, 0, 0.01, 1000);
    }
}