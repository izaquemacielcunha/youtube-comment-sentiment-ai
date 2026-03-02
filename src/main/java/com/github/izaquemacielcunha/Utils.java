package com.github.izaquemacielcunha;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String YOUTUBE_VIDEO_URL_REGEX = "^https://www\\.youtube\\.com/watch\\?v=([a-zA-Z0-9_-]{11})$";
    private static final String YOUTUBE_VIDEO_URL_SHARED_REGEX = "^https://youtu\\.be/([a-zA-Z0-9_-]{11})(?:\\?.*)?$";
    private static final String YOUTUBE_VIDEO_URL_EMBED_REGEX = "^https://www\\.youtube\\.com/embed/([a-zA-Z0-9_-]{11})(?:\\?.*)?$";

    public static String extractVideoId(String url) {
        if(url.matches(YOUTUBE_VIDEO_URL_REGEX)) {
            Pattern pattern = Pattern.compile(YOUTUBE_VIDEO_URL_REGEX);
            Matcher matcher = pattern.matcher(url);

            if (matcher.matches()) {
                return matcher.group(1);
            }
            return  null;
        } else if (url.matches(YOUTUBE_VIDEO_URL_SHARED_REGEX)) {
            Pattern pattern = Pattern.compile(YOUTUBE_VIDEO_URL_SHARED_REGEX);
            Matcher matcher = pattern.matcher(url);

            if (matcher.matches()) {
                return matcher.group(1);
            }
            return  null;
        } else if (url.matches(YOUTUBE_VIDEO_URL_EMBED_REGEX)) {
            Pattern pattern = Pattern.compile(YOUTUBE_VIDEO_URL_EMBED_REGEX);
            Matcher matcher = pattern.matcher(url);

            if (matcher.matches()) {
                return matcher.group(1);
            }
            return  null;
        }
        return null;
    }
}
