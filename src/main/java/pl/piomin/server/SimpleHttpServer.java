package pl.piomin.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleHttpServer {

    final Logger LOG = Logger.getLogger("main");
    final Integer cpus = Runtime.getRuntime().availableProcessors();

    HttpServer httpServer;

    public void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/example", new SimpleCPUConsumeHandler());
        if (System.getenv("THREAD_TYPE") == null || System.getenv("THREAD_TYPE").equals("virtual"))
            httpServer.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        else
            httpServer.setExecutor(Executors.newFixedThreadPool(100));
        httpServer.start();
        LOG.log(Level.INFO, "Server Started (port->8080, cpus->{0})", new Object[] {cpus});
    }

    public void stop() {
        if (httpServer != null) {
            httpServer.stop(100);
        }
    }
}
