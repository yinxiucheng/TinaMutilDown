package tina.com.common.http.utils;

import android.app.Activity;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author YuSongLin
 * @description toast
 */
@SuppressWarnings("unused")
public class ToastUtil {
    /**
     * 弹出Toast
     *
     * @param resId Toast内容对应的资源
     */
    public static void show(@StringRes int resId) {
        Toast.makeText(Utils.getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出Toast
     *
     * @param text Toast内容
     */
    public static void show(String text) {
        if (TextUtils.isEmpty(text)) return;
        Toast.makeText(Utils.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据指定时长弹出Toast
     */
    public static void show(String text, int length) {
        if (TextUtils.isEmpty(text)) return;
        if (length < 0 || length > 1) {
            length = Toast.LENGTH_SHORT;
        }
        Toast toast = Toast.makeText(Utils.getContext(), text, Toast.LENGTH_SHORT);
        toast.setDuration(length);
        toast.show();
    }

    /**
     * 在屏幕中央弹出Toast
     */
    public static void showInCenter(String text) {
        if (TextUtils.isEmpty(text)) return;
        try {
            Toast toast = Toast.makeText(Utils.getContext(), text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void toastOnUIThread(Activity activity, final String content, final int duration) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            show(content, duration);
        } else {
            if (activity != null) {
                activity.runOnUiThread(() -> show(content, duration));
            }
        }
    }

}
