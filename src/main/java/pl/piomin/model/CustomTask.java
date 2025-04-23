package pl.piomin.model;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public record CustomTask(long startTime, String msg, long id) implements Delayed {

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = startTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.startTime - ((CustomTask) o).startTime);
    }
}
