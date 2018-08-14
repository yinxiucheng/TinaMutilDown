package tina.com.common.download.utils;

import java.io.Closeable;
import java.io.IOException;


public class IOCloseUtils {

    public static void close(Closeable closeable) throws IOException {
        if (closeable != null) {
            synchronized (IOCloseUtils.class) {
                closeable.close();
            }
        }
    }
}
