package me.lhfcws.multiprocessing;

import java.io.*;

/**
 * me.lhfcws.multiprocessing.RunnerMain
 *
 * @author lhfcws
 * @since 2017/4/7
 */
public class RunnerMain {
    public static void main(String[] args) throws Exception {
        String fn = args[0];
        Serializable result;
        int exitValue = 0;

        try {
            // read runnerBytes
            FileInputStream fis = new FileInputStream(fn);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int b = 0;
            while ((b = fis.read()) != -1) {
                bos.write(b);
            }
            bos.flush();
            fis.close();

            byte[] runnerBytes = bos.toByteArray();
            bos.close();

            // delete input file
//            File inputFile = new File(fn);
//            if (inputFile.exists())
//                inputFile.delete();

            // deserialize to Runner & run
            Runner runner = (Runner) RunnerUtil.deserialize(runnerBytes);
            result = runner.run();
        } catch (Exception exc) {
            result = exc;
            exc.printStackTrace();
            exitValue = -1;
        }

        // create result file
        if (result != null) {
            String resFn = fn + RunnerUtil.RES_SUFFIX;
            FileOutputStream fos = new FileOutputStream(resFn);
            RunnerUtil.serialize2OutputStream(fos, result);
        }

        System.exit(exitValue);
    }
}
