package pl.piomin.serverless;

import pl.piomin.server.SimpleHttpServer;

import java.io.IOException;

public class MainApp {

    public static void main(String[] args) throws IOException {
        new SimpleHttpServer().start();
    }
}
