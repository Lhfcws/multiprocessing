package me.lhfcws.multiprocessing;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * me.lhfcws.multiprocessing.Pool
 *
 * @author lhfcws
 * @since 2017/3/28
 */
public class Pool implements Serializable {
    /**
     * 0 normal / running
     * -1 stop
     * -2 terminate
     */
    private AtomicInteger status = new AtomicInteger(0);
    private int maxSize = 10;
    private Semaphore poolBoundLock;
    private PoolChecker poolChecker;
    ConcurrentHashMap<String, ProcessFuture> pool = new ConcurrentHashMap<>();


    public Pool(int maxSize) {
        if (maxSize <= 0)
            throw new RuntimeException("Pool maxSize should be a postive integer.");
        this.maxSize = maxSize;
        poolBoundLock = new Semaphore(maxSize);
        poolChecker = new PoolChecker(this);
        poolChecker.run();
    }

    public int getMaxSize() {
        return maxSize;
    }

    /**
     * kill
     */
    public void stop() {
        status.compareAndSet(0, -1);
    }

    /**
     * kill -9
     */
    public void terminate() {
        status.set(-2);
        LinkedList<ProcessFuture> futures = new LinkedList<>(pool.values());
        pool.clear();
        for (ProcessFuture future : futures) {
            future.cancel(true);
        }
    }

    public Future<Serializable> submit(Runner runner) throws Exception {
        if (status.get() != 0) {
            throw new IOException("Process pool is ended.");
        }

        poolBoundLock.acquire();

        // serialize runner
        byte[] runnerBytes = RunnerUtil.serialize(runner);
        String md5 = Md5Util.md5(runnerBytes);
        final String fn;

        String name = runner.getClass().getCanonicalName();
        if (name == null)
            name = runner.getClass().getName();
        synchronized (this) {
            long ts = System.nanoTime();
            fn = name + "." + ts + RunnerUtil.SER_SUFFIX;
        }

        // write runner out
        FileOutputStream fos = new FileOutputStream(fn);
        fos.write(runnerBytes);
        fos.flush();
        fos.close();

        // launch RunnerMain
        final Process process = new RunnerLauncher().launchRunner(fn);
        ProcessFuture future = new ProcessFuture(fn, process, new Runnable() {
            @Override
            public void run() {
                pool.remove(fn);
                poolBoundLock.release();
            }
        });
        pool.put(future.getId(), future);
        return future;
    }

    /**
     * wait for stop/terminate finished
     */
    public void join() throws IllegalStateException, InterruptedException {
        if (status.get() == 0) {
            throw new IllegalStateException("stop or terminate should be invoked before join.");
        }

        while (!pool.isEmpty()) {
            Thread.sleep(1000);
        }
    }

    public boolean isOn() {
        return status.get() == 0;
    }

    public boolean isCompletelyStopped() {
        return !isOn() && pool.isEmpty();
    }
}
