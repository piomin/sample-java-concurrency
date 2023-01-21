package pl.piomin.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class SimpleHttpServerTests {

    final Logger LOG = Logger.getLogger("test");

    @Test
    void start() throws IOException, URISyntaxException, InterruptedException {
        SimpleHttpServer httpServer = new SimpleHttpServer();
        httpServer.start();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/example"))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNotNull(response.body());
        LOG.info("Response received: " + response.body());
        Assertions.assertTrue(response.body().startsWith("Hello_"));
    }
}
