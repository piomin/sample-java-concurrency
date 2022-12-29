package pl.piomin.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleCPUConsumeHandler implements HttpHandler {

    Logger LOG = Logger.getLogger("handler");
    AtomicLong i = new AtomicLong();
    final Integer cpus = Runtime.getRuntime().availableProcessors();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        new BigInteger(200, 3, new Random());
        String response = "Hello_" + i.incrementAndGet();
//        LOG.log(Level.INFO, "(CPU->{0}) {1}", new Object[] {cpus, response});
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
