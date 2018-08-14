package tina.com.live_base.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.support.annotation.StringRes;

/**
 * <p>Utils初始化相关 </p>
 */
public class BaseUtils {

    private static Context context;

    private BaseUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        BaseUtils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    /**
     * 全局获取String的方法
     *
     * @param id 资源Id
     * @return String
     */
    public static String getString(@StringRes int id) {
        return context.getResources().getString(id);
    }

    /**
     * 获取Resource资源
     *
     * @return
     */
    public static Resources getResources() {
        return context.getResources();
    }

    public static <T> T checkNotNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    /**
     * 判断当前app是否处于调试状态
     * @return boolean
     */
    public static boolean isDebug() {
        boolean isDebug = false;
        try {
            ApplicationInfo info = BaseUtils.getContext().getApplicationInfo();
            isDebug = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            // nothing
        }
        LogUtil.d("CommonWebView:isDebug:" + isDebug);
        return isDebug;
    }

}