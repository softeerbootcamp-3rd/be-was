package common.utils;

import common.logger.CustomLogger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncExecutor {

    private final BlockingQueue<Runnable> eventQueue;
    private final ExecutorService executorService;

    // volatile 키워드를 추가하여 여러 스레드에서 안전하게 접근할 수 있도록 함
    private volatile boolean running;


    private AsyncExecutor() {
        // LinkedBlockingQueue 를 사용하여 큐가 비어 있을 때 take() 메서드가 요소가 들어올 때까지 자동으로 대기하도록함
        // 이로 인해 불필요한 CPU 사용과 고정된 대기 시간이 제거됨
        this.eventQueue = new LinkedBlockingQueue<>();
        this.executorService = Executors.newSingleThreadExecutor();
        this.running = true;
        startEventProcessing();
    }

    private static class SingletonHelper {

        private static final AsyncExecutor SINGLETON = new AsyncExecutor();
    }

    public static AsyncExecutor getInstance() {
        return SingletonHelper.SINGLETON;
    }

    private void startEventProcessing() {
        executorService.submit(() -> {
            while (running || !eventQueue.isEmpty()) {
                try {
                    // take() 메서드는 큐에서 요소가 사용 가능해질 때까지 대기
                    Runnable event = eventQueue.take();
                    CustomLogger.printInfo("event processing");
                    event.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 인터럽트 처리
                    break; // 인터럽트 발생 시 while 루프 종료
                } catch (Exception e) {
                    CustomLogger.printError(e); // 예외 로깅
                }
            }
        });
    }

    public void addEvent(Runnable event) {
        eventQueue.add(event);
    }

    public void shutdown() {
        running = false;
        executorService.shutdownNow();
    }
}

