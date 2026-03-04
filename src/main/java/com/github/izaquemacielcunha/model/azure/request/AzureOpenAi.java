package com.github.izaquemacielcunha.model.azure.request;

import java.util.List;

public record AzureOpenAi(
        List<Message> contents,
        int temperature,
        double top_p,
        int max_tokens
) {
    public AzureOpenAi(List<Message> contents) {
        this(contents, 0, 0.01, 1000);
    }
}