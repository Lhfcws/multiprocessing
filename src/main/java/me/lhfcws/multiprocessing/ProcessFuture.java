package me.lhfcws.multiprocessing;

import java.io.*;
import java.util.concurrent.*;

/**
 * me.lhfcws.multiprocessing.ProcessFuture
 *
 * @author lhfcws
 * @since 2017/4/7
 */
public class ProcessFuture extends FutureTask<Serializable> {
    private Runnable callback = null;
    private Process process;
    private String id;

    ProcessFuture(String id, Process process, Runnable callback) {
        super(buildCallable(id, process));
        this.process = process;
        this.callback = callback;
        this.id = id;
    }

    @Override
    protected void done() {
        if (this.callback != null) {
            this.callback.run();
            this.callback = null;
        }
    }

    public String getId() {
        return id;
    }

    public Process getProcess() {
        return process;
    }

    private static Callable buildCallable(final String id, final Process process) {
        return new Callable<Serializable>() {
            @Override
            public Serializable call() throws Exception {
                int flag = process.waitFor();
                try {
                    if (flag == 0) {
                        InputStream in = new FileInputStream(id + RunnerUtil.RES_SUFFIX);
                        Object res = RunnerUtil.deserialize(in);
                        return (Serializable) res;
                    } else
                        throw new ExecutionException(new Exception("process exit code = " + flag));
                } catch (FileNotFoundException e) {
                    return null;
                } catch (ClassNotFoundException | IOException e) {
                    throw new ExecutionException(e);
                }
            }
        };
    }
}
