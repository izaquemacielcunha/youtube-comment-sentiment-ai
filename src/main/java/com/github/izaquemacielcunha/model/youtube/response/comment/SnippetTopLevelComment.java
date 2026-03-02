package com.github.izaquemacielcunha.model.youtube.response.comment;

import java.time.Instant;

public record SnippetTopLevelComment(
        String textDisplay,
        AuthorChannelId authorChannelId,
        String authorDisplayName,
        Instant publishedAt,
        Instant updatedAt
) { }