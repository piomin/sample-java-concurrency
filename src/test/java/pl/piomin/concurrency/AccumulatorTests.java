package pl.piomin.concurrency;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

import static org.hamcrest.Matchers.equalTo;

public class AccumulatorTests {

    @Test
    void accumulator() {
        LongAccumulator balance = new LongAccumulator(Long::sum, 10000L);
        Runnable w = () -> balance.accumulate(1000L);

        ExecutorService executor = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 50; i++) {
            executor.submit(w);
        }

        Awaitility.given().pollExecutorService(executor)
                .await()
                .atMost(1000, TimeUnit.MILLISECONDS)
                .untilAccumulator(balance, equalTo(60000L));
    }

    @Test
    void adder() {
        LongAdder balance = new LongAdder();
        Runnable w = () -> balance.add(1000L);

        ExecutorService executor = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 50; i++) {
            executor.submit(w);
        }

        Awaitility.given().pollExecutorService(executor)
                .await()
                .atMost(1000, TimeUnit.MILLISECONDS)
                .untilAdder(balance, equalTo(50000L));
    }

    @Test
    void atomic() {
        AtomicLong balance = new AtomicLong();
        Runnable w = () -> balance.addAndGet(1000L);

        ExecutorService executor = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 50; i++) {
            executor.submit(w);
        }

        Awaitility.given().pollExecutorService(executor)
                .await()
                .atMost(1000, TimeUnit.MILLISECONDS)
                .untilAtomic(balance, equalTo(50000L));
    }
}
