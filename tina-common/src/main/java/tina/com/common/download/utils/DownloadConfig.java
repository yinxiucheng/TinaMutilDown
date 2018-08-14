package tina.com.common.download.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by xiuchengyin on 2018/4/19.
 */

public class DownloadConfig {

    private String downloadPath = "";

    private File dwonloadDir;

    private static final String DOWNLOAD_DIR = "download";

    public static final int DEFAULT_MAX_THREAD_NUMBER = 10;

    public static final int DEFAULT_THREAD_NUMBER = 1;

    private static final int MAX_QUE_LENGTH = 2;

    private boolean useExternalStorageDir = true;

    //downloadService启动重新下载所有的任务
    private boolean recoverDownloadWhenStart = true;
    /**
     * thread number in the pool
     */
    private int maxThreadNum = DEFAULT_MAX_THREAD_NUMBER;

    //多任务下载的个数
    private int maxQueueLength = MAX_QUE_LENGTH;

    /**
     * thread number for each download
     */
    private int threadNum = DEFAULT_THREAD_NUMBER;

    private volatile static DownloadConfig instance;

    private DownloadConfig() {}

    public static DownloadConfig getInstance(){
        if (null == instance){
            synchronized (DownloadConfig.class){
                if (null == instance){
                    instance = new DownloadConfig();
                }
            }
        }
        return instance;
    }

    public boolean isUseExternalStorageDir() {
        return useExternalStorageDir;
    }

    public void setDownLoadPath(String path, boolean useExternalStorageDir) {
        this.useExternalStorageDir = useExternalStorageDir;
        downloadPath = path;
        dwonloadDir = new File(downloadPath);
    }

    public File getDownloadDir() {
        if (TextUtils.isEmpty(downloadPath)) {
            return getDefaultDownloadDir();
        } else {
            return new File(downloadPath);
        }
    }

    public File getDefaultDownloadDir() {
        dwonloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return dwonloadDir;
    }

    public File getDownloadFile(String fileName){
        return new File(dwonloadDir, fileName);
    }

    public int getMaxThreadNum() {
        return maxThreadNum;
    }

    public void setMaxThreadNum(int maxThreadNum) {
        this.maxThreadNum = maxThreadNum;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public boolean isRecoverDownloadWhenStart() {
        return recoverDownloadWhenStart;
    }

    //todo service开启时，自动恢复所有的任务
    public void setRecoverDownloadWhenStart(boolean recoverDownloadWhenStart) {
        this.recoverDownloadWhenStart = recoverDownloadWhenStart;
    }

    public int getMaxQueueLength() {
        return maxQueueLength;
    }

    public void setMaxQueueLength(int maxQueueLength) {
        this.maxQueueLength = maxQueueLength;
    }

}
