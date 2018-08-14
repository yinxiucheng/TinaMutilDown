package tina.com.common.download.utils;

import android.util.Log;

/**
 * Created by Stay on 5/8/15.
 * Powered by www.stay4it.com
 */
public class Trace {

    public static final String TAG = "download";
    private static final boolean DEBUG = true;

    public static void d(String msg) {
        if (DEBUG)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (DEBUG)
            Log.e(TAG, msg);
    }
}
