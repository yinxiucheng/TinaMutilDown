package tina.com.common.download;

import android.content.Context;

import tina.com.common.download.entity.DownloadInfo;
import tina.com.common.download.entity.DownloadStatus;
import tina.com.common.download.observer.DataChanger;
import tina.com.common.download.observer.DataWatcher;

/**
 * @author yxc
 * @date 2018/8/1
 */
public class DownloadManager {

    private static DownloadManager instance;
    private Context context;

    private DownloadManager(Context context){
        this.context = context;
    }

    public static DownloadManager getInstance(Context context){
        if (null == instance){
            synchronized (DownloadManager.class){
                if (null == instance){
                    instance = new DownloadManager(context);
                }
            }
        }
        return instance;
    }

    public void download(DownloadInfo downloadInfo){
        DownloadService.intentDownload(context, downloadInfo);
    }

    public void pause(DownloadInfo downloadInfo){
        DownloadService.intentPause(context, downloadInfo);
    }

    public void resume(DownloadInfo downloadInfo){
        DownloadService.intentResume(context, downloadInfo);
    }

    public void cancel(DownloadInfo downloadInfo){
        DownloadService.intentCancel(context, downloadInfo);
    }

    public void pauseAll(){
        DownloadService.intentPauseAll(context);
    }

    public void recoverAll(){
        DownloadService.intentRecoverAll(context);
    }

    public void cancelAll(){
        DownloadService.intentRecoverAll(context);
    }

//    private DownloadInfo getDownLoadInfo(String url){
//        DownloadInfo downloadInfo = hashMap.get(url);
//        if (null == downloadInfo){
//            downloadInfo = new DownloadInfo(url, DownloadInfo.DEFAULT_PROCESS_BEGIN, DownloadStatus.IDLE);
//            hashMap.put(url, downloadInfo);
//            //todo 是否有数据库操作?
//        }
//        return downloadInfo;
//    }

    public void addObservable(Context context, DataWatcher dataWatcher){
        DataChanger.getInstance(context).addObserver(dataWatcher);
    }

    public void removeObservable(Context context, DataWatcher dataWatcher){
        DataChanger.getInstance(context).deleteObserver(dataWatcher);
    }

}
