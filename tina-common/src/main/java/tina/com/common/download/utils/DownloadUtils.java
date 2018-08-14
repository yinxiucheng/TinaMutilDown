package tina.com.common.download.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.StrictMode;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;

import tina.com.common.download.entity.DownloadInfo;

/**
 * Created by Aspsine on 2015/7/29.
 */
public class DownloadUtils {

    public static final String TAG = "Utils";

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public static String getDownloadPerSize(long finished, long total) {
        return DF.format((float) finished / (1024 * 1024)) + "M/" + DF.format((float) total / (1024 * 1024)) + "M";
    }

    public static String getDownloadEndSize(long total) {
        return DF.format((float) total / (1024 * 1024)) + "M";
    }

    public static void installApp(Context context, String fileName) {
        File file = new File(DownloadConfig.getInstance().getDefaultDownloadDir(), fileName);
        Trace.d("install apk :" + file.getPath());
        installApp(context, file);
    }

    public static void installApp(Context context, File file) {

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void openApp(Context context, String packageName) {
        if (DownloadUtils.isAppInstalled(context, packageName)) {
            PackageManager packageManager = context.getPackageManager();
            Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(packageName);
            if (launchIntentForPackage != null) {
                context.startActivity(launchIntentForPackage);
            } else {
                Toast.makeText(context, "手机未安装该应用", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "手机未安装该应用", Toast.LENGTH_SHORT).show();
        }
    }

    public static void unInstallApp(Context context, String packageName) {
        Uri packageUri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String getApkFilePackage(Context context, File apkFile) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkFile.getPath(), PackageManager.GET_ACTIVITIES);
        if (info != null) {
            return info.applicationInfo.packageName;
        }
        return null;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        if (packages != null && packages.size() > 0) {
            for (PackageInfo packageInfo : packages) {
                if (packageInfo.packageName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getVersionCode(Context context, String packageName) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        if (packages != null && packages.size() > 0) {
            for (PackageInfo packageInfo : packages) {
                if (packageInfo.packageName.equals(packageName)) {
                    return packageInfo.versionCode;
                }
            }
        }
        return Integer.MIN_VALUE;
    }


    public static void deleteAppInfo(File file) {
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    public static String getFileName(String url, DownloadInfo appInfo) {
        if (TextUtils.isEmpty(url) || appInfo == null) {
            Trace.d( "getFileName() called with: url = [" + url + "], appInfo = [" + appInfo + "]");
            return getTime() + ".apk";
        }
        String packageName = "";
        try {
            packageName = appInfo.getPackageName().replace(".", "_");
            String result = String.valueOf(url.hashCode()) + "_" + packageName + "_" + appInfo.getVersionCode() + ".apk";
            Trace.d("getFileName() called with: result = [" + result + "], appInfo = [" + appInfo + "]");
            return result;
        } catch (Exception e) {
            Trace.d("getFileName() called with: Exception = [" + e.toString() + "], appInfo = [" + appInfo + "]");
            return "Download_" + packageName + "_" + appInfo.getVersionCode() + ".apk";
        }
    }

    public static String getTime() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        String str = String.valueOf(time);
        return str;
    }


    private static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
            && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


    //生成 Key
    public static String createKey(String tag) {
        if (tag == null) {
            throw new NullPointerException("Tag can't be null!");
        }
        return String.valueOf(tag.hashCode());
    }


    //获取可用SD卡存储
    public static long getEnableSize() {
        File path = Environment.getExternalStorageDirectory();
        if (null == path) {
            return 0;
        }
        StatFs stat = new StatFs(path.getPath());
        long blockSize;  //每一个存储块的大小
        long tatalBlocks;  //存储块的总数
        long avaliableBlocks;  //可以使用的存储块的个数

        //获取当前的版本的等级,版本大于18也就是4.4.2，使用没过时的api
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();  //每一个存储块的大小
            tatalBlocks = stat.getBlockCountLong();  //存储块的总数
            avaliableBlocks = stat.getAvailableBlocksLong();  //可以使用的存储块的个数

        } else {
            blockSize = stat.getBlockSizeLong();  //每一个存储块的大小
            tatalBlocks = stat.getBlockCountLong();  //存储块的总数
            avaliableBlocks = stat.getAvailableBlocksLong();  //可以使用的存储块的个数
        }
        return blockSize * avaliableBlocks;
    }

}
