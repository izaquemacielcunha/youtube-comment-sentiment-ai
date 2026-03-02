package com.github.izaquemacielcunha.model.youtube.response.comment;

public record Snippet(
        String channelId,
        String videoId,
        TopLevelComment topLevelComment
) { }
