package by.pashkavlushka.GoodsCatalogueService.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddUpdateFallbackRequestExecutor {

    private static ExecutorService addExecutorService = Executors.newVirtualThreadPerTaskExecutor();
    private static ExecutorService updateExecutorService = Executors.newVirtualThreadPerTaskExecutor();


    public static void submitAddTaskFallback(Runnable runnable) {
        addExecutorService.submit(runnable);
    }

    public static void submitUpdateTaskFallback(Runnable runnable) {
        updateExecutorService.submit(runnable);
    }
}
