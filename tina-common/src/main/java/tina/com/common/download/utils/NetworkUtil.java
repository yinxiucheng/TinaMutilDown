package tina.com.common.download.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.ResponseBody;
import tina.com.common.downloadback.buffer.DownInfo;

/**
 * @author: 小民
 * @date: 2017-06-06
 * @time: 14:53
 * @开源地址: https://github.com/2745329043/XDownloadDemo
 * @说明: 文件操作类
 */
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

    /**
     * 描述：判断网络是否有效.
     *
     * @return true, if is network available
     */
    public boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
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

    /**
     * 写入文件    异步下载时，写入
     * @param file
     * @param info
     * @throws IOException
     */
    public void writeCache(ResponseBody responseBody, File file, DownInfo info) throws IOException {
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        long allLength = info.getCountLength();
        if (allLength == 0) {
            allLength = responseBody.contentLength();
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
        FileChannel channelOut = randomAccessFile.getChannel();
        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, info.getReadLength(), allLength - info.getReadLength());
        InputStream inputStream = responseBody.byteStream();
        byte[] buffer = new byte[1024 * 8];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            mappedBuffer.put(buffer, 0, len);
        }
        //关闭流
        inputStream.close();
        channelOut.close();
        randomAccessFile.close();
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
