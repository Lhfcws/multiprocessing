package me.lhfcws.multiprocessing.test;

import me.lhfcws.multiprocessing.Pool;
import me.lhfcws.multiprocessing.Runner;

import java.io.*;
import java.util.Date;

/**
 * me.lhfcws.multiprocessing.test.TestSubmit
 *
 * @author lhfcws
 * @since 2017/5/30
 */
public class TestSubmit implements Serializable {
    static Pool pool = new Pool(1);

    public void testSubmit() throws IOException, InterruptedException {
        new File("test1.txt").delete();
        final int i = 1;
        try {
            Runner runner = new Runner() {
                @Override
                public Serializable run() throws Exception {
                    System.out.println(i);
                    Pool pool0 = pool;
                    System.out.println(pool0.toString());
                    OutputStream os = new FileOutputStream("test1.txt");
                    os.write(new Date().toString().getBytes());
                    os.flush();
                    os.close();
                    return null;
                }
            };
            System.out.println(runner.getClass().getCanonicalName());
            pool.submit(runner);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pool.stop();
        pool.join();

        System.out.println(new File("test1.txt").exists());
    }

    public static void main(String[] args) throws Exception {
        new TestSubmit().testSubmit();
    }
}
