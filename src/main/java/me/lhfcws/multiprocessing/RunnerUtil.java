package me.lhfcws.multiprocessing;

import java.io.*;

/**
 * me.lhfcws.multiprocessing.RunnerUtil
 *
 * @author lhfcws
 * @since 2017/4/7
 */
public class RunnerUtil {
    public static final String RES_SUFFIX = ".res";
    public static final String SER_SUFFIX = ".sclass";

    public static void serialize2OutputStream(OutputStream os, Serializable obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(obj);
        oos.flush();
        os.flush();
        oos.close();
        os.close();
    }

    public static byte[] serialize(Serializable obj) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(obj);
        oos.flush();
        os.flush();
        oos.close();
        os.close();
        return os.toByteArray();
    }

    public static Object deserialize(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream oin = new ObjectInputStream(in);
        Object obj = oin.readObject();
        oin.close();
        in.close();
        return obj;
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bin);
        Object obj = in.readObject();
        in.close();
        bin.close();
        return obj;
    }
}
