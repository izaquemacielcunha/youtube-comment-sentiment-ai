package com.github.izaquemacielcunha.model.azure.response;

import java.util.List;

public record AzureOpenAIRootResponse(
        List<Choice> choices,
        String id
) { }
