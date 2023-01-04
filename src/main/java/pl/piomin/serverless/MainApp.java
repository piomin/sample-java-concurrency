package pl.piomin.serverless;

import com.sun.net.httpserver.HttpServer;
import pl.piomin.server.SimpleCPUConsumeHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainApp {

    final static Logger LOG = Logger.getLogger("main");
    final static Integer cpus = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer
                .create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/example", new SimpleCPUConsumeHandler());
        if (System.getenv("THREAD_TYPE").equals("virtual"))
            httpServer.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        else
            httpServer.setExecutor(Executors.newFixedThreadPool(100));
        httpServer.start();
        LOG.log(Level.INFO, "Server Started (port->8080, cpus->{0})", new Object[] {cpus});
    }
}
