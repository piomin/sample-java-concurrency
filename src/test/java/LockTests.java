import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import pl.piomin.model.Balance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

public class LockTests {

    @Test
    void stamped() {
        StampedLock lock = new StampedLock();
        Balance b = new Balance(10000);
        Runnable w = () -> {
            long stamp = lock.writeLock();
            b.setAmount(b.getAmount() + 1000);
            System.out.println("Write: " + b.getAmount());
            lock.unlockWrite(stamp);
        };
        Runnable r = () -> {
            long stamp = lock.tryOptimisticRead();
            if (!lock.validate(stamp)) {
                stamp = lock.readLock();
                try {
                    System.out.println("Read: " + b.getAmount());
                } finally {
                    lock.unlockRead(stamp);
                }
            } else {
                System.out.println("Optimistic read fails");
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 50; i++) {
            executor.submit(w);
            executor.submit(r);
        }

        Awaitility.given().pollExecutorService(executor)
                .await()
                .atMost(1000, TimeUnit.MILLISECONDS);
    }
}
