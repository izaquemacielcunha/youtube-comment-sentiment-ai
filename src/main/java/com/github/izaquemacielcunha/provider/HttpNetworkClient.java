package com.github.izaquemacielcunha.provider;

import java.net.http.HttpClient;
import java.time.Duration;

public class HttpNetworkClient {

    private final static HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static HttpClient getHttpClient() {
        return httpClient;
    }

}// end of class