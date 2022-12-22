package pl.piomin.server;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleWork {

    AtomicLong id = new AtomicLong();
    ReentrantLock lock = new ReentrantLock();

    public String doJob() {
        String response = null;
        lock.lock();
        try {
            Thread.sleep(100);
            response = "Ping_" + id.incrementAndGet();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        } finally {
            lock.unlock();
        }
        return response;
    }
}
