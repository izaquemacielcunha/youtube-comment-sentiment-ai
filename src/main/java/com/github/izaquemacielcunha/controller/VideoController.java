package com.github.izaquemacielcunha.controller;

import com.github.izaquemacielcunha.Utils;
import com.github.izaquemacielcunha.model.Mapper;
import com.github.izaquemacielcunha.model.VideoRequest;
import com.github.izaquemacielcunha.model.azure.AzureOpenAIResponse;
import com.github.izaquemacielcunha.model.azure.request.AzureOpenAIRequest;
import com.github.izaquemacielcunha.model.youtube.response.comment.YouTubeVideoCommentsResponse;
import com.github.izaquemacielcunha.model.youtube.response.video.YouTubeVideoMetadataResponse;
import com.github.izaquemacielcunha.service.AzureOpenAIService;
import com.github.izaquemacielcunha.service.YouTubeService;
import com.github.izaquemacielcunha.validation.VideoUrlValidator;
import io.javalin.http.Context;

import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class VideoController {
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

    private final YouTubeService youTubeService;
    private final AzureOpenAIService azureOpenAIService;
    private final VideoUrlValidator videoUrlValidator;
    private final Mapper mapper;

    public VideoController(YouTubeService youTubeService, AzureOpenAIService azureOpenAIService,
                           VideoUrlValidator videoUrlValidator, Mapper mapper) {
        this.youTubeService = youTubeService;
        this.azureOpenAIService = azureOpenAIService;
        this.videoUrlValidator = videoUrlValidator;
        this.mapper = mapper;
    }

    public void getCommentsSentiment(Context context) {
        VideoRequest videoRequest = context.bodyAsClass(VideoRequest.class);

        List<String> errors = videoUrlValidator.validate(videoRequest.url());

        if (errors.isEmpty()) {

            String videoId = Utils.extractVideoId(videoRequest.url());
            YouTubeVideoCommentsResponse youTubeVideoCommentsResponse;
            YouTubeVideoMetadataResponse youTubeVideoMetadataResponse;
            AzureOpenAIResponse response;

            try {
                youTubeVideoCommentsResponse = getComments(videoId);
                youTubeVideoMetadataResponse = getMetadata(videoId);
                response = getAzureOpenAIAnalysis(youTubeVideoCommentsResponse,  youTubeVideoMetadataResponse);
            }
            catch (Exception exception) {
                context.status(HttpStatus.INTERNAL_SERVER_ERROR).json(Map.of("message", exception.getMessage()));
                return;
            }

            context.status(HttpStatus.OK).json(response);// TODO
            return;
        }

        logger.info("[getCommentsSentiment] - Ai pai, para, erro {}", errors.getFirst());
        context.status(HttpStatus.BAD_REQUEST).json(Map.of("message", errors.getFirst()));
    }

    private YouTubeVideoCommentsResponse getComments(String videoId) {
        YouTubeVideoCommentsResponse response;

        try {
            response = youTubeService.getVideoComments(videoId);
        }
        catch (Exception exception) {
            //TODO melhorar isso
            throw new RuntimeException(exception);
        }

        return response;
    }

    private YouTubeVideoMetadataResponse getMetadata(String videoId) {
        YouTubeVideoMetadataResponse response;

        try {
            response = youTubeService.getVideoMetadata(videoId);
        }
        catch (Exception exception) {
            //TODO melhorar isso
            throw new RuntimeException(exception);
        }

        return response;
    }

    private AzureOpenAIResponse getAzureOpenAIAnalysis(YouTubeVideoCommentsResponse videoCommentsResponse,
                                                       YouTubeVideoMetadataResponse videoMetadataResponse) {
        AzureOpenAIResponse response;
        AzureOpenAIRequest azureOpenAiBodyRequest = mapper.mapToAzureOpenAiRequest(videoCommentsResponse, videoMetadataResponse);

        try {
            response = azureOpenAIService.getVideoSentiment(azureOpenAiBodyRequest);
        }
        catch (Exception exception) {
            //TODO melhorar isso
            throw new RuntimeException(exception);
        }

        return response;
    }

}