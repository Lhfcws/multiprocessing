package me.lhfcws.multiprocessing;

import com.sun.akuma.JavaVMArguments;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * me.lhfcws.multiprocessing.RunnerLauncher
 *
 * @author lhfcws
 * @since 2017/4/7
 */
public class RunnerLauncher {
    public Process launchRunner(String fn) throws IOException {
        JavaVMArguments arguments = JavaVMArguments.current();
        arguments.set(arguments.size() - 1, RunnerMain.class.getCanonicalName());
        arguments.add(fn);
        String[] cmd = new String[arguments.size()];
        cmd = arguments.toArray(cmd);
        System.out.println(arguments);

        Process process = Runtime.getRuntime().exec(cmd);
        System.out.println(getPid(process));
        return process;
    }

    public static String getPid(Process process) {
        Field field = null;
        try {
            field = process.getClass().getDeclaredField("pid");
            field.setAccessible(true);
            Object processID = field.get(process);
            return String.valueOf(processID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
