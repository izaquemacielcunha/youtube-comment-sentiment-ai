package com.github.izaquemacielcunha.service.impl;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.izaquemacielcunha.model.youtube.YouTubeCredentials;
import com.github.izaquemacielcunha.model.youtube.response.comment.YouTubeVideoCommentsResponse;
import com.github.izaquemacielcunha.model.youtube.response.video.YouTubeVideoMetadataResponse;

import com.github.izaquemacielcunha.service.YouTubeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.json.JsonMapper;

public class YouTubeServiceImpl implements YouTubeService {
    private static final Logger logger = LoggerFactory.getLogger(YouTubeServiceImpl.class);

    private static final String YOUTUBE_VIDEO_COMMENTS_PATH = "/commentThreads";
    private static final String YOUTUBE_VIDEO_PATH = "/videos";

    private final YouTubeCredentials credentials;
    private final HttpClient httpClient;
    private final JsonMapper objectMapper;

    public YouTubeServiceImpl(YouTubeCredentials credentials, HttpClient httpClient, JsonMapper objectMapper) {
        this.credentials = credentials;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public YouTubeVideoCommentsResponse getVideoComments(String videoId) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getFullUrlVideoComments(videoId)))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        //TODO validate response code

        String body = response.body();

        logger.debug("[getVideoComments] - Response from youtube service: {}", body);

        return objectMapper.fromJsonString(body, YouTubeVideoCommentsResponse.class);
    }

    public YouTubeVideoMetadataResponse getVideoMetadata(String videoId) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getFullUrlVideoMetadata(videoId)))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        //TODO validate response code

        String body = response.body();

        logger.debug("[getVideoMetadata] - Response from youtube service: {}", body);

        return objectMapper.fromJsonString(body, YouTubeVideoMetadataResponse.class);
    }

    private String getFullUrlVideoComments(String videoId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("part", "snippet");
        parameters.put("videoId", encodeUrlParam(videoId));
        parameters.put("maxResults", "30");
        parameters.put("order", "relevance");
        parameters.put("key", encodeUrlParam(credentials.token()));

        String queryString = parameters
                .entrySet()
                .stream()
                .map(
                        entry -> entry.getKey() + "=" + entry.getValue()
                )
                .collect(Collectors.joining("&"));

        return credentials.url() + YOUTUBE_VIDEO_COMMENTS_PATH + "?" + queryString;
    }

    private String getFullUrlVideoMetadata(String videoId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("part", "snippet");
        parameters.put("id", encodeUrlParam(videoId));
        parameters.put("key", encodeUrlParam(credentials.token()));

        String queryString = parameters
                .entrySet()
                .stream()
                .map(
                        entry -> entry.getKey() + "=" + entry.getValue()
                )
                .collect(Collectors.joining("&"));

        return credentials.url() + YOUTUBE_VIDEO_PATH + "?" + queryString;
    }

    private String encodeUrlParam(String param) {
        return URLEncoder.encode(param, StandardCharsets.UTF_8);
    }
}
