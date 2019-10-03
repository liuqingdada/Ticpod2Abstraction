package com.mobvoi.android.common.async;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.mobvoi.android.common.utils.DateTimeUtils;
import com.mobvoi.android.common.utils.LogUtil;
import com.mobvoi.android.common.utils.Preconditions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings({"unused", "WeakerAccess"})
public class TaskScheduler {

    private static final String TAG = "TaskScheduler";
    private static final boolean DEBUG_LOGGING = false;

    private static final int MSG_ADD_TASK = 1;
    private static final int MSG_REMOVE_TASK = 2;
    private static final int MSG_CHECK_TASKS = 3;
    private static final int MSG_CLEAR_TASKS = 4;

    private static final int DEFAULT_CHECK_INTERVAL = 10_000; // 10 seconds

    private ITaskExecutor mTaskExecutor;
    private String mOwnerTag;
    private long mCheckInterval = DEFAULT_CHECK_INTERVAL;
    private boolean mShowLog = true;

    private Handler mMainHandler = new MainHandler();
    private ArrayList<TaskInfo> mTasks = new ArrayList<>();

    public TaskScheduler(@NonNull ITaskExecutor executor, @NonNull String ownerTag) {
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(ownerTag);
        mTaskExecutor = executor;
        mOwnerTag = ownerTag;
    }

    public void setCheckInterval(long interval) {
        if (interval < 1000) {
            throw new IllegalArgumentException("Interval less than 1 second is not allowed.");
        }
        mCheckInterval = interval;
    }

    public void showLog(boolean show) {
        this.mShowLog = show;
    }

    public void schedule(@NonNull Runnable task, long delayedMs) {
        TaskInfo taskInfo = new TaskInfo(task, delayedMs);
        if (mShowLog) {
            LogUtil.d(TAG, "[%s] schedule one-off task: %s", mOwnerTag, taskInfo);
        }
        scheduleTask(taskInfo);
    }

    public void schedule(@NonNull Runnable task, long delayedMs, long periodMs) {
        TaskInfo taskInfo = new TaskInfo(task, delayedMs, periodMs);
        if (mShowLog) {
            LogUtil.d(TAG, "[%s] schedule period task: %s", mOwnerTag, taskInfo);
        }
        scheduleTask(taskInfo);
    }

    private void scheduleTask(TaskInfo taskInfo) {
        if (isMainThread()) {
            addTask(taskInfo);
        } else {
            mMainHandler.obtainMessage(MSG_ADD_TASK, taskInfo).sendToTarget();
        }
    }

    public void cancelTask(@NonNull Runnable task) {
        if (mShowLog) {
            LogUtil.d(TAG, "[%s] cancel task: %s", mOwnerTag, task);
        }
        if (isMainThread()) {
            removeTask(task);
        } else {
            mMainHandler.obtainMessage(MSG_REMOVE_TASK, task).sendToTarget();
        }
    }

    public void clear() {
        if (mShowLog) {
            LogUtil.d(TAG, "[%s] clear tasks", mOwnerTag);
        }
        if (isMainThread()) {
            clearTasks();
        } else {
            mMainHandler.sendEmptyMessage(MSG_CLEAR_TASKS);
        }
    }

    public void trigger() {
        if (mShowLog) {
            LogUtil.d(TAG, "[%s] trigger checking", mOwnerTag);
        }
        if (isMainThread()) {
            checkTasks();
        } else {
            mMainHandler.sendEmptyMessage(MSG_CHECK_TASKS);
        }
    }

    @MainThread
    private void addTask(TaskInfo task) {
        mTasks.add(task);
        scheduleCheckTask(task.delay);
    }

    @MainThread
    private void removeTask(@NonNull Runnable task) {
        for (int i = 0; i < mTasks.size(); /* empty */) {
            TaskInfo info = mTasks.get(i);
            if (info.task.equals(task)) {
                if (mShowLog) {
                    LogUtil.d(TAG, "[%s] task removed: %s", mOwnerTag, info);
                }
                mTasks.remove(i);
            } else {
                i++;
            }
        }
    }

    @MainThread
    private void checkTasks() {
        if (mTasks.isEmpty()) {
            if (mShowLog) {
                LogUtil.d(TAG, "[%s] Tasks empty, cancel check.", mOwnerTag);
            }
            mMainHandler.removeMessages(MSG_CHECK_TASKS);
            return;
        }

        if (DEBUG_LOGGING && mShowLog) {
            LogUtil.v(TAG, "[%s] check tasks, taskCount: %d", mOwnerTag, mTasks.size());
        }
        Iterator<TaskInfo> it = mTasks.iterator();
        ArrayList<Runnable> pendingTasks = new ArrayList<>();
        long nextEventDelay = mCheckInterval;
        while (it.hasNext()) {
            TaskInfo info = it.next();
            if (SystemClock.elapsedRealtime() >= info.triggerAt) {
                if (mShowLog) {
                    LogUtil.d(TAG, "[%s] execute task: %s", mOwnerTag, info);
                }
                pendingTasks.add(info.task);
                if (info.period > 0) {
                    info.triggerAt = SystemClock.elapsedRealtime() + info.period;
                } else {
                    it.remove();
                    info = null; // mark it removed from queue
                }
            }

            if (info != null) {
                long timeout = info.triggerAt - SystemClock.elapsedRealtime();
                if (timeout < nextEventDelay) {
                    nextEventDelay = timeout;
                }
            }
        }

        if (DEBUG_LOGGING && mShowLog) {
            LogUtil.v(TAG, "[%s] next check at %s", mOwnerTag,
                    DateTimeUtils.getReadableTimeStamp(System.currentTimeMillis() + nextEventDelay));
        }
        mMainHandler.removeMessages(MSG_CHECK_TASKS);
        mMainHandler.sendEmptyMessageDelayed(MSG_CHECK_TASKS, nextEventDelay);

        if (pendingTasks.size() > 0) {
            mTaskExecutor.postTasks(pendingTasks);
        }
    }

    @MainThread
    private void clearTasks() {
        mTasks.clear();
        mTaskExecutor.clearTasks();
    }

    @MainThread
    private void scheduleCheckTask(long delay) {
        if (delay > mCheckInterval) {
            delay = mCheckInterval;
        }
        mMainHandler.sendEmptyMessageDelayed(MSG_CHECK_TASKS, delay);
    }

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    @SuppressLint("HandlerLeak")
    private class MainHandler extends Handler {
        MainHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ADD_TASK: {
                    TaskInfo task = (TaskInfo) msg.obj;
                    addTask(task);
                    break;
                }

                case MSG_REMOVE_TASK: {
                    Runnable task = (Runnable) msg.obj;
                    removeTask(task);
                    break;
                }

                case MSG_CHECK_TASKS: {
                    checkTasks();
                    break;
                }

                case MSG_CLEAR_TASKS: {
                    clearTasks();
                    break;
                }
            }
        }
    }
}

class TaskInfo {
    private static AtomicInteger sTaskId = new AtomicInteger(1);

    private int taskId;
    public Runnable task;
    public long delay;
    public long period = -1;
    public long triggerAt;

    TaskInfo(@NonNull Runnable task, long delay) {
        this.taskId = sTaskId.getAndIncrement();
        this.task = task;
        this.delay = delay;
        this.triggerAt = SystemClock.elapsedRealtime() + delay;
    }

    TaskInfo(@NonNull Runnable task, long delay, long period) {
        this.taskId = sTaskId.getAndIncrement();
        this.task = task;
        this.delay = delay;
        this.period = period;
        this.triggerAt = SystemClock.elapsedRealtime() + delay;
    }

    @Override
    public String toString() {
        long timestamp = System.currentTimeMillis() - (SystemClock.elapsedRealtime() - triggerAt);
        return "TaskInfo[id=" + taskId + ", delay=" + delay
                + ", triggerAt=" + DateTimeUtils.getReadableTimeStamp(timestamp)
                + ", period=" + period + "]";
    }
}
