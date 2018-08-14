package com.tina;

import android.app.Application;
import android.os.Environment;

import java.io.File;

import tina.com.common.download.utils.DownloadConfig;
import tina.com.common.http.utils.Utils;
import tina.com.live_base.utils.BaseUtils;

/**
 * @author yxc
 * @date 2018/8/2
 */
public class TinaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        BaseUtils.init(this);
        initDownloader();
        setDownloadDir();
    }


    private void initDownloader() {
//        DownloadDataConfig configuration = new DownloadDataConfig();
        DownloadConfig.getInstance().setMaxThreadNum(10);
        DownloadConfig.getInstance().setThreadNum(3);
    }

    //设置下载路径
    public static void setDownloadDir(){
        File mDownloadDir = new File(Environment.getExternalStorageDirectory(), "download");
        DownloadConfig.getInstance().setDownLoadPath(mDownloadDir.getPath(), true);
    }
}
