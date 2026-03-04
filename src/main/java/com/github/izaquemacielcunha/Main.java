package com.github.izaquemacielcunha;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.github.izaquemacielcunha.controller.VideoController;
import com.github.izaquemacielcunha.model.Mapper;
import com.github.izaquemacielcunha.model.azure.AzureOpenAICredentials;
import com.github.izaquemacielcunha.model.youtube.YouTubeCredentials;
import com.github.izaquemacielcunha.provider.HttpNetworkClient;
import com.github.izaquemacielcunha.provider.JsonObjectMapper;
import com.github.izaquemacielcunha.service.AzureOpenAIService;
import com.github.izaquemacielcunha.service.YouTubeService;
import com.github.izaquemacielcunha.service.impl.AzureOpenAIServiceImpl;
import com.github.izaquemacielcunha.service.impl.YouTubeServiceImpl;
import com.github.izaquemacielcunha.validation.VideoUrlValidator;
import io.javalin.Javalin;

import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Main {
    public static void main(String[] args) {
        final var youTubeCredentials = new YouTubeCredentials(System.getenv("YOUTUBE_API_URL"), System.getenv("YOUTUBE_API_KEY"));
        final YouTubeService youTubeService = new YouTubeServiceImpl(youTubeCredentials, HttpNetworkClient.getHttpClient(), JsonObjectMapper.getMapper());
        final AzureOpenAICredentials azureOpenAiCredentials = new AzureOpenAICredentials(System.getenv("AZURE_OPEN_AI_API_URL"), System.getenv("AZURE_OPEN_AI_API_KEY"), System.getenv("AZURE_OPEN_AI_API_VERSION"));
        final AzureOpenAIService azureOpenAIService = new AzureOpenAIServiceImpl(azureOpenAiCredentials, HttpNetworkClient.getHttpClient(), JsonObjectMapper.getMapper());
        final var videoUrlValidator = new VideoUrlValidator();
        final var mapper = new Mapper();
        final var videoController = new VideoController(youTubeService, azureOpenAIService, videoUrlValidator, mapper);

        var app = Javalin.create(config -> {
            config.concurrency.useVirtualThreads = true;

            config.routes.apiBuilder(() -> {
                path("/video", () ->{
                    post("/comment/sentiment", videoController::getCommentsSentiment);
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