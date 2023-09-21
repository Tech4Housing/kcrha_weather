package org.kcrha.weather.collectors;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpService {
    protected HttpResponse<String> getRetryableResponse(String url, Integer maxAttempts) throws URISyntaxException, IOException, InterruptedException {
        int attemptCount = 0;
        while (attemptCount < maxAttempts) {
            HttpClient client = getClient();

            HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().timeout(Duration.ofSeconds(10)).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response;
            } else {
                attemptCount += 1;
            }
        }

        throw new RuntimeException(String.format("Failed to retrieve API response for %s after %s attempts", url, maxAttempts));
    }

    protected HttpResponse<String> getResponse(HttpRequest request) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = getClient();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpClient getClient() {
        return HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).followRedirects(HttpClient.Redirect.NORMAL).build();
    }
}
