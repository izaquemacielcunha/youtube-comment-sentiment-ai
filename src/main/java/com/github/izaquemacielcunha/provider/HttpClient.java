package com.github.izaquemacielcunha.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient {
    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    private final static HttpClient httpClient = null;
    
    public static HttpClient getHttpClient() {
        return httpClient;
    }

}