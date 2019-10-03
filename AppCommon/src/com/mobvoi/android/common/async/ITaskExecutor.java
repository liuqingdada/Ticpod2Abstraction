package com.mobvoi.android.common.async;

import androidx.annotation.NonNull;

import java.util.List;

public interface ITaskExecutor {
    /**
     * Post a task to execute.
     * <p />
     * This method should return immediately and the task should be executed asynchronously.
     */
    void postTasks(@NonNull List<Runnable> tasks);

    /**
     * Clear all pending tasks.
     */
    void clearTasks();
}
