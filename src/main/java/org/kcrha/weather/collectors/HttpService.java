package org.kcrha.weather.collectors;

import org.apache.commons.lang3.StringUtils;
import org.kcrha.weather.collectors.exceptions.MaxAttemptsExceededException;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

public class HttpService {
    protected HttpResponse<String> getRetryableResponse(String url, Integer maxAttempts) throws URISyntaxException, IOException, InterruptedException {
        int attemptCount = 0;
        while (attemptCount < maxAttempts) {
            HttpClient client = getClient();

            HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().timeout(Duration.ofSeconds(15)).build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    return response;
                } else {
                    System.out.printf("Received non-200 response from URL: %s\n", url);
                }
            } catch (SSLHandshakeException | HttpTimeoutException e) {
                if (StringUtils.isBlank(url)) {
                    System.out.println("Blank URL!");
                } else {
                    System.out.printf("Encountered %s when querying URL: %s\n", e, url);
                }
            } finally {
                attemptCount += 1;
            }
        }

        throw new MaxAttemptsExceededException(String.format("Failed to retrieve API response for %s after %s attempts", url, maxAttempts));
    }

    protected HttpResponse<String> getResponse(HttpRequest request) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = getClient();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpClient getClient() {
        return HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).followRedirects(HttpClient.Redirect.NORMAL).build();
    }
}
