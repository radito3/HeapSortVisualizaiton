package sample.controller;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

class AsyncServiceSupplier {

    private static Supplier<ScheduledExecutorService> supplier = () -> {
        ScheduledThreadPoolExecutor service = new ScheduledThreadPoolExecutor(2);
        service.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        service.setRemoveOnCancelPolicy(true);
        service.setMaximumPoolSize(5);
        service.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        service.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        return service;
    };

    static ScheduledExecutorService get() {
        return supplier.get();
    }
}
