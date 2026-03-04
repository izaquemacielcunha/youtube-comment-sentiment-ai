package com.github.izaquemacielcunha.service;

import com.github.izaquemacielcunha.model.youtube.response.comment.YouTubeVideoCommentsResponse;
import com.github.izaquemacielcunha.model.youtube.response.video.YouTubeVideoMetadataResponse;

public interface YouTubeService {
    YouTubeVideoCommentsResponse getVideoComments(String videoId) throws Exception;
    YouTubeVideoMetadataResponse getVideoMetadata(String videoId) throws Exception;
}
