package com.glee.mxrecyclerview;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liji
 * @date 2018/12/24 11:46
 * description
 */


public class TaskExecutor {
    private TaskExecutor() {
    }

    public static TaskExecutor getInstance() {
        return TaskExecutorHolder.INSTANCE;
    }

    public static Executor getIoExecutor() {
        return IoExecutorHolder.INSTANCE;
    }

    public static Executor getMainExecutor() {
        return MainExecutorHolder.INSTANCE;
    }

    public static void postToMainThread(@NonNull Runnable runnable) {
        MainExecutorHolder.INSTANCE.execute(runnable);
    }

    public static void executeOnIO(@NonNull Runnable runnable) {
        IoExecutorHolder.INSTANCE.execute(runnable);
    }

    private static final class TaskExecutorHolder {
        private static final TaskExecutor INSTANCE = new TaskExecutor();
    }

    private static final class IoExecutorHolder {
        private static final Executor INSTANCE = new ThreadPoolExecutor(
                4, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(), Executors.defaultThreadFactory());
    }

    @SuppressLint("NewApi")
    private static final class MainExecutorHolder {
        private static Handler MAIN_HANDLER;
        private static boolean asyncMessage;
        private static final int MAIN = 100;


        private static final Executor INSTANCE = new Executor() {
            {
                MAIN_HANDLER = new Handler(Looper.getMainLooper(), msg -> {
                    if (msg.what == MAIN) {
                        ((Runnable) msg.obj).run();
                        return true;
                    }
                    return false;
                });
                Message message = Message.obtain();
                try {
                    message.setAsynchronous(true);
                    asyncMessage = true;
                } catch (Exception e) {
                    asyncMessage = false;
                    e.printStackTrace();
                }
                message.recycle();
            }

            @Override
            public void execute(Runnable command) {
                Message message = MAIN_HANDLER.obtainMessage(MAIN, command);
                message.setAsynchronous(asyncMessage);
                message.sendToTarget();
            }
        };
    }
}
