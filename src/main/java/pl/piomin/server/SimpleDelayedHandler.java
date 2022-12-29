package pl.piomin.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SimpleDelayedHandler implements HttpHandler {

    private final List<SimpleWork> workers = new ArrayList<>();
    private final int workersCount = 50;
    private final boolean withLock;
    AtomicLong id = new AtomicLong();

    public SimpleDelayedHandler(boolean withLock) {
        this.withLock = withLock;
        if (withLock) {
            for (int i = 0; i < workersCount; i++) {
                workers.add(new SimpleWork());
            }
        }
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = null;
        if (withLock) {
            response = workers.get((int) (id.incrementAndGet() % workersCount)).doJob();
        } else {
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            new BigInteger(500, 10, new Random());
            response = "Ping_" + id.incrementAndGet();
        }

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
