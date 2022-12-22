package org.example;

import com.sun.net.httpserver.HttpServer;
import pl.piomin.server.SimpleDelayedHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException {
        runServer(true, true);
    }

    private static void runServer(boolean virtual, boolean withLock) throws IOException {
        HttpServer httpServer = HttpServer
                .create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/example", new SimpleDelayedHandler(withLock));
        if (virtual) {
            httpServer.setExecutor(
                    Executors.newVirtualThreadPerTaskExecutor());
        } else {
            httpServer.setExecutor(
                    Executors.newFixedThreadPool(200));
        }
        httpServer.start();
    }
}