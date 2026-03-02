package com.github.izaquemacielcunha.model.youtube.response.video;

import java.util.List;

public record YouTubeVideoMetadataResponse(
        List<Item> items,
        PageInfo pageInfo
) { }
