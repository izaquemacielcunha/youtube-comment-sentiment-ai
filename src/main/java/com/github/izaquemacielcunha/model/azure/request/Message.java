package com.github.izaquemacielcunha.model.azure.request;

import java.util.List;

public record Message(
        String role,
        List<Content> content
) { }
