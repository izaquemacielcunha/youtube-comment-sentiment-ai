package com.github.izaquemacielcunha;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.github.izaquemacielcunha.controller.VideoController;
import com.github.izaquemacielcunha.model.youtube.YouTubeCredentials;
import com.github.izaquemacielcunha.provider.HttpNetworkClient;
import com.github.izaquemacielcunha.provider.JsonObjectMapper;
import com.github.izaquemacielcunha.service.YouTube;
import com.github.izaquemacielcunha.validation.VideoUrlValidator;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Main {
    public static void main(String[] args) {
        final var credentials = new YouTubeCredentials(System.getenv("YOUTUBE_API_URL"), System.getenv("YOUTUBE_API_KEY"));
        final var youTube = new YouTube(credentials, HttpNetworkClient.getHttpClient(), JsonObjectMapper.getMapper());
        final var videoUrlValidator = new VideoUrlValidator();
        final var videoController = new VideoController(videoUrlValidator, youTube);

        var app = Javalin.create(config -> {
            config.concurrency.useVirtualThreads = true;

            config.routes.apiBuilder(() -> {
                path("/video", () ->{
                    post("/comment/sentiment", videoController::getCommentsSentiment);
                    post("/metadata", videoController::getMetadata);
                });
            });

            config.jsonMapper(JsonObjectMapper.getMapper());

            config.routes.exception(IllegalArgumentException.class, (e, ctx) -> {
                // TODO Add mapper to json
                ctx.status(400).json(e.getMessage());
                ctx.json(e.getMessage());
            });

            config.routes.exception(UnrecognizedPropertyException.class, (e, ctx) -> {
                ctx.status(500).json(Map.of("message", e.getMessage()));
            });

            config.routes.exception(JsonParseException.class, (e, ctx) -> {
                ctx.status(500).json(e.getMessage());
                ctx.json(e.getMessage());
            });

            // TODO general error
            config.routes.error(404, context -> {
                context.status(404).json(Map.of("message", "Not found bicho"));
            });

        }).start();

    }
}