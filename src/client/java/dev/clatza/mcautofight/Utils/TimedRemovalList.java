package dev.clatza.mcautofight.Utils;

import java.util.concurrent.*;

public class TimedRemovalList {
    private ConcurrentHashMap<Integer, ScheduledFuture<?>> map = new ConcurrentHashMap<>();
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public void add(Integer value) {
        remove(value);

        ScheduledFuture<?> scheduledFuture = executorService.schedule(() -> {
            map.remove(value);
            System.out.println("Eintrag entfernt: " + value);
        }, 2, TimeUnit.MINUTES);

        map.put(value, scheduledFuture);
    }

    public void remove(Integer value) {
        ScheduledFuture<?> future = map.remove(value);
        if (future != null) {
            future.cancel(false);
        }
    }

    public boolean contains(Integer value) {
        return map.containsKey(value);
    }

    public void shutdown() {
        executorService.shutdownNow();
    }
}