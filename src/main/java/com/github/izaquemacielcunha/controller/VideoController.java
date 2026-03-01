package com.github.izaquemacielcunha.controller;

import com.github.izaquemacielcunha.model.VideoRequest;
import com.github.izaquemacielcunha.validation.VideoUrlValidator;
import io.javalin.http.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class VideoController {
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

    private final VideoUrlValidator videoUrlValidator;

    public VideoController(VideoUrlValidator videoUrlValidator) {
        this.videoUrlValidator = videoUrlValidator;
    }

    public static void aloBrasil(Context context) {
        logger.info("YOUTUBE_API_URL {}", System.getenv("YOUTUBE_API_URL"));
        logger.info("YOUTUBE_API_KEY {}", System.getenv("YOUTUBE_API_KEY"));
        logger.info("AZURE_LLM_API_URL {}", System.getenv("AZURE_LLM_API_URL"));
        logger.info("AZURE_LLM_API_KEY {}", System.getenv("AZURE_LLM_API_KEY"));
    }

    public void getCommentsSentiment(Context context) {
        VideoRequest videoRequest = context.bodyAsClass(VideoRequest.class);

        List<String> errors = videoUrlValidator.validate(videoRequest.url());

        if (errors.isEmpty()) {
            logger.info("Ai pai, para {}",  videoRequest.toString());
            context.status(200).json(Map.of("message", "Deu certo cara de batata"));
            return;
        }
        context.status(500).json(Map.of("message", errors.getFirst()));
    }
}