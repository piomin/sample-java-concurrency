package org.example;

import com.sun.net.httpserver.HttpServer;
import org.openjdk.jmh.annotations.*;
import pl.piomin.server.SimpleDelayedHandler;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws IOException {
        runServer(true, false);
//        org.openjdk.jmh.Main.main(args);
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

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(warmups = 1, value = 20)
    @Warmup(iterations = 1)
    public void generate() {
        new BigInteger(1000, 5, new Random());
    }
}