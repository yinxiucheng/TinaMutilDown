package tina.com.common.download.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;


public class NetworkUtil {
    private static NetworkUtil mFileUtil;
    private Context mContext;
    private String mApplictionId;

    public NetworkUtil(Context context, String appliction_id) {
        this.mContext = context;
        this.mApplictionId = appliction_id;
    }

    public static NetworkUtil init(Context context, String applictionId){
        if(mFileUtil == null){
            mFileUtil = new NetworkUtil(context,applictionId);
        }
        return mFileUtil;
    }

    public static NetworkUtil getInstance(){
        return mFileUtil;
    }

    /** 下载文件保存路径  */
    public File getDownloadDirectory(){
        File appCacheDir;
        //判断SDK存不存在
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            appCacheDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }else{
            appCacheDir = Environment.getDownloadCacheDirectory();
        }
        return appCacheDir;
    }

    /** 安装apk */
    public void installApk(String path) {
        File apkFile = new File(path);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            // 声明需要的临时权限
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 第二个参数，即第一步中配置的authorities
            Uri contentUri = FileProvider.getUriForFile(mContext, mApplictionId + ".fileProvider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }


}
