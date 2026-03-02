package com.github.izaquemacielcunha.controller;

import com.github.izaquemacielcunha.Utils;
import com.github.izaquemacielcunha.model.VideoRequest;
import com.github.izaquemacielcunha.model.youtube.response.comment.YouTubeVideoCommentsResponse;
import com.github.izaquemacielcunha.model.youtube.response.video.YouTubeVideoMetadataResponse;
import com.github.izaquemacielcunha.service.YouTube;
import com.github.izaquemacielcunha.validation.VideoUrlValidator;
import io.javalin.http.Context;

import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class VideoController {
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

    private final VideoUrlValidator videoUrlValidator;
    private final YouTube youTube;

    public VideoController(VideoUrlValidator videoUrlValidator, YouTube youTube) {
        this.videoUrlValidator = videoUrlValidator;
        this.youTube = youTube;
    }

    public void getCommentsSentiment(Context context) {
        VideoRequest videoRequest = context.bodyAsClass(VideoRequest.class);

        List<String> errors = videoUrlValidator.validate(videoRequest.url());

        if (errors.isEmpty()) {
            YouTubeVideoCommentsResponse response;

            try {
                response = youTube.getVideoComments(Utils.extractVideoId(videoRequest.url()));
            }
            catch (InterruptedException | IOException exception) {
                context.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of("message", exception.getMessage()));
                return;
            }

            context.status(HttpStatus.OK).json(response);// TODO
            return;
        }

        logger.info("[getCommentsSentiment] - Ai pai, para, erro {}", errors.getFirst());
        context.status(HttpStatus.BAD_REQUEST).json(Map.of("message", errors.getFirst()));
    }

    public void getMetadata(Context context) {
        VideoRequest videoRequest = context.bodyAsClass(VideoRequest.class);

        List<String> errors = videoUrlValidator.validate(videoRequest.url());

        if (errors.isEmpty()) {
            YouTubeVideoMetadataResponse response;

            try {
                response = youTube.getVideoMetadata(Utils.extractVideoId(videoRequest.url()));
            }
            catch (InterruptedException | IOException exception) {
                context.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of("message", exception.getMessage()));
                return;
            }

            context.status(HttpStatus.OK).json(response);// TODO
            return;
        }

        logger.info("[getMetadata] - Ai pai, para, erro {}", errors.getFirst());
        context.status(HttpStatus.BAD_REQUEST).json(Map.of("message", errors.getFirst()));
    }
}