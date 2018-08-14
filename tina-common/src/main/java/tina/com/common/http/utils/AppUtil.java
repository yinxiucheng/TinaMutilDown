package tina.com.common.http.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import tina.com.live_base.utils.LogUtil;

/**
 * @author Ragnar
 * @date 2018/5/21 下午7:09
 * @description app相关工具
 */
public class AppUtil {

    /**
     * 获取version code
     */
    public static int getAppVersionCode() {
        PackageInfo info = getPackageInfo();

        if (info == null) {
            return 0;
        } else {
            return info.versionCode;
        }
    }

    /**
     * 获取包信息
     */
    public static PackageInfo getPackageInfo() {
        return getPackageInfo(Utils.getContext().getPackageName());
    }

    /**
     * 获取对应包名的包信息
     */
    public static PackageInfo getPackageInfo(final String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = Utils.getContext().getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo;
    }


    /**
     * 获取包名
     *
     * @return 获取不到返回""
     */
    public static String getAppPackageName() {
        PackageInfo info = getPackageInfo();

        if (info == null) {
            return "";
        } else {
            return info.packageName;
        }
    }

    public static boolean isNeedAccessToken(String url){
        return true;
    }


    /**
     * 启动一个app
     * com -- ComponentName 对象，包含apk的包名和主Activity名
     * param -- 需要传给apk的参数
     */
    public static void startApp(Context context, String packageName) {
        LogUtil.d("startApp context=" + context + " packageName= " + packageName);
        if (!TextUtils.isEmpty(packageName) && context != null) {
            try {
                Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                context.startActivity(LaunchIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
