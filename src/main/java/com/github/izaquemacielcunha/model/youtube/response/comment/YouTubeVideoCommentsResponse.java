package com.github.izaquemacielcunha.model.youtube.response.comment;

import java.util.List;

public record YouTubeVideoCommentsResponse(
        PageInfo pageInfo,
        List<Item> items
) { }// end of class
