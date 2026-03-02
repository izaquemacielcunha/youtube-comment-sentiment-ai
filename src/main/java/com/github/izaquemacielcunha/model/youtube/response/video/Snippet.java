package com.github.izaquemacielcunha.model.youtube.response.video;

import java.time.Instant;
import java.util.List;

public record Snippet(
        String channelId,
        String channelTitle,
        Instant publishedAt,
        String title,
        String description,
        Thumbnails thumbnails,
        List<String> tags
) { }