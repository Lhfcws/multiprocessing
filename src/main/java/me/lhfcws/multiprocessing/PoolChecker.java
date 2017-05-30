package me.lhfcws.multiprocessing;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * me.lhfcws.multiprocessing.PoolChecker
 *
 * @author lhfcws
 * @since 2017/5/29
 */
public class PoolChecker implements Serializable {
    Thread thread;
    Pool pool;

    public PoolChecker(Pool pool) {
        this.pool = pool;
    }

    public void run() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (!pool.isCompletelyStopped()) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    LinkedList<ProcessFuture> toRemove = new LinkedList<>();
                    LinkedList<ProcessFuture> list = new LinkedList<>(pool.pool.values());
                    for (ProcessFuture future : list) {
                        try {
                            int exitValue = future.getProcess().exitValue();
                            pool.pool.remove(future.getId());
                        } catch (IllegalThreadStateException ignore) {

                        }
                    }

                }
                System.out.println(pool.toString() + " is off.");
            }
        });
        thread.start();
    }

    public void stop() {
        if (thread != null)
            thread.interrupt();
    }
}
