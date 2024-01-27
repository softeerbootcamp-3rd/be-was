package common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncExecutor {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(8);

    public static void execute(Runnable task) {
        executorService.execute(task);
    }

    public static void shutdown() {
        executorService.shutdown();
    }
}

