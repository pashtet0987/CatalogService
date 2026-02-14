package by.pashkavlushka.GoodsCatalogueService.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedbackRequestExecutor {

    private static ExecutorService addExecutorService = Executors.newVirtualThreadPerTaskExecutor();
    private static ExecutorService updateExecutorService = Executors.newVirtualThreadPerTaskExecutor();
    private static ExecutorService deleteExecutorService = Executors.newVirtualThreadPerTaskExecutor();

    public static void submitAddTaskFeedback(Runnable runnable) {
        addExecutorService.submit(runnable);
    }

    public static void submitUpdateTaskFeedback(Runnable runnable) {
        updateExecutorService.submit(runnable);
    }

    public static void submitDeleteTaskFeedback(Runnable runnable) {
        deleteExecutorService.submit(runnable);
    }
}
