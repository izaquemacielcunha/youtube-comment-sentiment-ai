package com.github.izaquemacielcunha.validation;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

public class VideoUrlValidator {

    private static final String YOUTUBE_VIDEO_URL_REGEX = "^https://www\\.youtube\\.com/watch\\?v=([a-zA-Z0-9_-]{11})$";
    private static final String YOUTUBE_VIDEO_URL_SHARED_REGEX = "^https://youtu\\.be/([a-zA-Z0-9_-]{11})(?:\\?.*)?$";
    private static final String YOUTUBE_VIDEO_URL_EMBED_REGEX = "^https://www\\.youtube\\.com/embed/([a-zA-Z0-9_-]{11})(?:\\?.*)?$";

    public List<String> validate(String url) {
        List<String> errors = new ArrayList<>();

        if (isNullOrBlank(url)) {
            errors.add("Video url is null or blank");
            return errors;
        }

        if (!isValidUrl(url)) {
            errors.add("Video url is invalid");
            return errors;
        }

        return errors;
    }

    private boolean isNullOrBlank(String url) {
        return isNull(url) || url.isBlank();
    }

    private boolean isValidUrl(String url) {
        return url.matches(YOUTUBE_VIDEO_URL_REGEX)
                || url.matches(YOUTUBE_VIDEO_URL_SHARED_REGEX)
                || url.matches(YOUTUBE_VIDEO_URL_EMBED_REGEX);
    }
}