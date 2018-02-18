package me.eranik.threads;

import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Thread pool that can take tasks and process them.
 * @param <T> type of tasks' result
 */
public class ThreadPool<T> {

    private int size;
    private Thread threads[];
    private LinkedList<ThreadPoolTask> tasks;

    /**
     * Constructs thread pool with specified number of threads.
     * @param size number of threads in thread pool
     */
    public ThreadPool(int size) {
        this.size = size;
        this.threads= new Thread[size];
        this.tasks = new LinkedList<>();

        for (int i = 0; i < size; i++) {
            this.threads[i] = new Thread(new ThreadPoolWorker());
            this.threads[i].setDaemon(true);
            this.threads[i].start();
        }
    }

    /**
     * Finishes all threads in thread pool.
     */
    public synchronized void shutdown() {
        for (int i = 0; i < size; i++) {
            threads[i].interrupt();
        }
    }

    /**
     * Receives a task and passes it to a free thread for processing.
     * @param task specified task to be processed
     * @return special object that stores this task and through which you can interact with thread pool
     */
    public synchronized LightFuture<T> addTask(Supplier<T> task) {
        ThreadPoolTask wrapper = new ThreadPoolTask(task);
        tasks.add(wrapper);
        return wrapper;
    }

    private class ThreadPoolTask implements LightFuture<T> {

        private Supplier<T> task;
        private boolean ready = false;
        private Object result = null;

        private ThreadPoolTask(Supplier<T> task) {
            this.task = task;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isReady() {
            return ready;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @SuppressWarnings("unchecked")
        public T get() {
            while (!ready) {
                Thread.yield();
            }
            if (result instanceof Exception) {
                throw new LightExecutionException((Exception) result);
            }
            return (T) result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LightFuture<T> thenApply(Function<T, T> function) {
            return ThreadPool.this.addTask(() -> function.apply(ThreadPoolTask.this.get()));
        }

        private void run() {
            result = task.get();
            ready = true;
        }
    }

    private class ThreadPoolWorker implements Runnable {
        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                ThreadPoolTask task;
                synchronized (ThreadPool.this) {
                    if (tasks.isEmpty()) {
                        continue;
                    }
                    task = tasks.remove();
                }
                if (task != null) {
                    try {
                        task.run();
                    } catch (Exception e) {
                        task.result = e;
                    }
                }
            }
        }
    }

}
