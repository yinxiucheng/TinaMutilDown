package tina.com.common.download.observer;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import tina.com.common.download.data.DBHelper;
import tina.com.common.download.entity.DownloadInfo;
import tina.com.common.download.entity.DownloadStatus;

/**
 * @author yxc
 * @date 2018/8/1
 */
public class DataChanger extends Observable {
    private volatile static DataChanger dataWatcher;
    private Context context;
    private LinkedHashMap<String, DownloadInfo> mOperateDownloadInfoList;
    private Executor executor;

    private DataChanger(Context context){
        this.context = context;
        mOperateDownloadInfoList = new LinkedHashMap<>();
        executor = Executors.newSingleThreadExecutor();
    }

    public static synchronized DataChanger getInstance(Context context){
        if (null == dataWatcher){
            synchronized (DataChanger.class){
                if (null == dataWatcher){
                    dataWatcher = new DataChanger(context);
                }
            }
        }
        return dataWatcher;
    }

    public void postStatus(DownloadInfo downloadInfo){
        mOperateDownloadInfoList.put(downloadInfo.tag, downloadInfo);
        executor.execute(() -> DBHelper.getInstance().newOrUpdate(downloadInfo));
        setChanged();
        notifyObservers(downloadInfo);
    }

    public List<DownloadInfo> queryAllRecoverableEntries() {
        List<DownloadInfo> mRecoverableEntries = DBHelper.getInstance().queryDownloadInfoAll();
        for (Map.Entry<String, DownloadInfo> entry : mOperateDownloadInfoList.entrySet()) {
            if (entry.getValue().status == DownloadStatus.PAUSED) {
                if (mRecoverableEntries == null) {
                    mRecoverableEntries = new ArrayList<>();
                }
                mRecoverableEntries.add(entry.getValue());
            }
        }
        return mRecoverableEntries;
    }

    public DownloadInfo queryDownloadEntryById(String tag) {
        return mOperateDownloadInfoList.get(tag);
    }

    public void addToOperatedEntryMap(String tag, DownloadInfo value){
        mOperateDownloadInfoList.put(tag, value);
    }

    public boolean containsDownloadEntry(String tag) {
        return mOperateDownloadInfoList.containsKey(tag);
    }

    public void deleteDownloadEntry(String tag){
        mOperateDownloadInfoList.remove(tag);
        //todo 数据库删除
//      DBHelper.getInstance().deleteDownloadInfoByTag(tag);
    }



}
