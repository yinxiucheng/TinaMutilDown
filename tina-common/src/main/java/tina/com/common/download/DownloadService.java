package tina.com.common.download;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import tina.com.common.download.core.DownloadTask;
import tina.com.common.download.core.Downloader;
import tina.com.common.download.data.DBHelper;
import tina.com.common.download.entity.DownloadInfo;
import tina.com.common.download.entity.DownloadStatus;
import tina.com.common.download.observer.DataChanger;
import tina.com.common.download.utils.Constants;
import tina.com.common.download.utils.DownloadConfig;
import tina.com.common.download.utils.DownloadUtils;
import tina.com.common.http.utils.Utils;

/**
 * @author yxc
 * @date 2018/8/1
 */
public class DownloadService extends Service implements Downloader.OnDownloaderDestroyedListener{

    private static final String TAG = DownloadService.class.getSimpleName();

    public static final int NOTIFY_DOWNLOADING = 1;

    public static final int NOTIFY_PAUSE_OR_CANCEL = 2;

    public static final int NOTIFY_COMPLETE = 3;

    public static final int NOTIFY_CONNECTING = 4;

    public static final int NOTIFY_DOWNLOAD_ERROR = 5;

    public static final String KEY_ACTION_DOWN = "action_down";

    public static final String EXTRA_TAG = "extra_tag";

    public static final String EXTRA_DOWNLOAD_INFO = "extra_down_info";

    private Executor executor;

    private HashMap<String, DownloadTask> mDownloadingTaskList = new HashMap<>();

    private LinkedBlockingDeque<DownloadInfo> mQueue = new LinkedBlockingDeque<>();

    private DataChanger mDataChanger;

    private DBHelper mDBHelper;

    private int maxQueueLength;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NOTIFY_PAUSE_OR_CANCEL:
                case NOTIFY_COMPLETE:
                    checkNextWaiting();
                    break;
                case NOTIFY_DOWNLOADING:{//todo progress 变更
                    break;
                }
            }

            DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
            if (null != downloadInfo){
                if (downloadInfo.status == DownloadStatus.COMPLETED
                        || downloadInfo.status == DownloadStatus.FAILED
                        || downloadInfo.status == DownloadStatus.PAUSED
                        || downloadInfo.status == DownloadStatus.CANCELED){
                    mDownloadingTaskList.remove(downloadInfo.getUrl());
                }
                mDataChanger.postStatus(downloadInfo);
            }
        }
    };


    private void checkNextWaiting() {
        if (mDownloadingTaskList.size() < maxQueueLength) {
            DownloadInfo downloadInfo = mQueue.poll();
            if (null != downloadInfo) {
                download(downloadInfo);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void intentDownload(Context context, DownloadInfo info) {
        generateKey(info);
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(KEY_ACTION_DOWN, Constants.ACTION_DOWNLOAD);
        intent.putExtra(EXTRA_TAG, info.getUrl());
        intent.putExtra(EXTRA_DOWNLOAD_INFO, info);
        context.startService(intent);
    }

    public static void intentPause(Context context, DownloadInfo info) {
        generateKey(info);
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(KEY_ACTION_DOWN, Constants.ACTION_PAUSE);
        intent.putExtra(EXTRA_DOWNLOAD_INFO, info);
        context.startService(intent);
    }

    public static void intentResume(Context context, DownloadInfo info) {
        generateKey(info);
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(KEY_ACTION_DOWN, Constants.ACTION_RESUME);
        intent.putExtra(EXTRA_DOWNLOAD_INFO, info);
        context.startService(intent);
    }


    public static void intentCancel(Context context, DownloadInfo info) {
        generateKey(info);
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(KEY_ACTION_DOWN, Constants.ACTION_CANCEL);
        intent.putExtra(EXTRA_DOWNLOAD_INFO, info);
        context.startService(intent);
    }

    public static void intentPauseAll(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(KEY_ACTION_DOWN, Constants.ACTION_PAUSE_ALL);
        context.startService(intent);
    }

    public static void intentRecoverAll(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(KEY_ACTION_DOWN, Constants.ACTION_RECVOER_ALL);
        context.startService(intent);
    }

    public static void intentCancelAll(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(KEY_ACTION_DOWN, Constants.ACTION_CANCEL_ALL);
        context.startService(intent);
    }

    private static void generateKey(DownloadInfo downloadInfo){
        if (null != downloadInfo && !TextUtils.isEmpty(downloadInfo.url)
                && TextUtils.isEmpty(downloadInfo.getTag())){
            String key = DownloadUtils.createKey(downloadInfo.url);
            downloadInfo.setTag(key);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        executor = Executors.newCachedThreadPool();
        mDataChanger = DataChanger.getInstance(Utils.getContext());
        mDBHelper = DBHelper.getInstance();
        maxQueueLength = DownloadConfig.getInstance().getMaxQueueLength();
        initalizeDownload();
    }

    private void initalizeDownload() {
        List<DownloadInfo> mDownloadEtries = DBHelper.getInstance().queryDownloadInfoAll();
        if (mDownloadEtries != null) {
            for (DownloadInfo downloadInfo : mDownloadEtries) {
                generateKey(downloadInfo);
                if (downloadInfo.status == DownloadStatus.DOWNLOADING || downloadInfo.status == DownloadStatus.WAITING) {
//                    TODO add a config if need to recover download
                    if (DownloadConfig.getInstance().isRecoverDownloadWhenStart()) {
                        if (downloadInfo.getAcceptRanges()){
                            downloadInfo.status = DownloadStatus.PAUSED;
                        }else {
                            downloadInfo.status = DownloadStatus.IDLE;
                            downloadInfo.reset();
                        }
                        addDownloadQueue(downloadInfo);
                    } else {
                        if (downloadInfo.getAcceptRanges()){
                            downloadInfo.status = DownloadStatus.PAUSED;
                        }else {
                            downloadInfo.status = DownloadStatus.IDLE;
                            downloadInfo.reset();
                        }
                        mDBHelper.newOrUpdate(downloadInfo);
                    }
                }
                mDataChanger.addToOperatedEntryMap(downloadInfo.tag, downloadInfo);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            int action = intent.getIntExtra(KEY_ACTION_DOWN, -1);
            DownloadInfo downloadInfo = (DownloadInfo) intent.getSerializableExtra(EXTRA_DOWNLOAD_INFO);
            if (downloadInfo != null && mDataChanger.containsDownloadEntry(downloadInfo.tag)) {
                downloadInfo = mDataChanger.queryDownloadEntryById(downloadInfo.tag);
            }
            doAction(action, downloadInfo);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void doAction(int action, DownloadInfo downloadInfo){
        switch (action) {
            case Constants.ACTION_DOWNLOAD:
                addDownloadQueue(downloadInfo);
                break;
            case Constants.ACTION_PAUSE:
                pause(downloadInfo);
                break;
            case Constants.ACTION_RESUME:
                resume(downloadInfo);
                break;
            case Constants.ACTION_CANCEL:
                cancle(downloadInfo);
                break;
            case Constants.ACTION_PAUSE_ALL:
                pauseAll();
                break;
            case Constants.ACTION_RECVOER_ALL:
                recoverAll();
                break;
            case Constants.ACTION_CANCEL_ALL:
                cancelAll();
                break;
            default:
        }
    }

    private void pause(DownloadInfo downloadInfo) {
        DownloadTask downloadTask = mDownloadingTaskList.remove(downloadInfo.getUrl());
        if (null != downloadTask) {
            downloadTask.pause();
        } else {
            mQueue.remove(downloadInfo);
            downloadInfo.setStatus(DownloadStatus.PAUSED);
            mDataChanger.postStatus(downloadInfo);
        }
    }

    private void resume(DownloadInfo downloadInfo) {
        addDownloadQueue(downloadInfo);
    }

    private void download(DownloadInfo downloadInfo) {
        DownloadTask task = new DownloadTask(downloadInfo, mHandler, executor, this);
        mDownloadingTaskList.put(downloadInfo.url, task);
        task.start();
    }

    private void addDownloadQueue(DownloadInfo downloadInfo) {
        if (mDownloadingTaskList.size() < maxQueueLength) {
            download(downloadInfo);
        } else {
            mQueue.add(downloadInfo);
            downloadInfo.setStatus(DownloadStatus.WAITING);
            mDataChanger.postStatus(downloadInfo);
        }
    }

    private void cancle(DownloadInfo downloadInfo) {
        DownloadTask downloadTask = mDownloadingTaskList.remove(downloadInfo.getUrl());
        if (null != downloadTask) {
            downloadTask.cancel();
        } else {
            mQueue.remove(downloadInfo);
            downloadInfo.setStatus(DownloadStatus.CANCELED);
            mDataChanger.postStatus(downloadInfo);
        }
    }

    private void pauseAll() {
        for (Map.Entry<String, DownloadTask> entry: mDownloadingTaskList.entrySet()) {
            entry.getValue().pause();
        }
        while (mQueue.iterator().hasNext()){
            DownloadInfo downloadInfo = mQueue.poll();
            downloadInfo.setStatus(DownloadStatus.PAUSED);
            DataChanger.getInstance(Utils.getContext()).postStatus(downloadInfo);
        }
        mDownloadingTaskList.clear();
    }

    private void recoverAll() {
        List<DownloadInfo> mRecoverableEntries = mDataChanger.queryAllRecoverableEntries();
        if (mRecoverableEntries != null) {
            for (DownloadInfo downloadInfo : mRecoverableEntries) {
                addDownloadQueue(downloadInfo);
            }
        }
    }

    //todo cancel 掉所有的请求, 清楚数据库，删除文件
    private void cancelAll() {
        for (Map.Entry<String, DownloadTask> entry: mDownloadingTaskList.entrySet()) {
            entry.getValue().cancel();
        }
        while (mQueue.iterator().hasNext()){
            DownloadInfo downloadInfo = mQueue.poll();
            downloadInfo.setStatus(DownloadStatus.CANCELED);
            DataChanger.getInstance(Utils.getContext()).postStatus(downloadInfo);
        }
        mDownloadingTaskList.clear();
    }

    @Override
    public void onDestroyed(String key, Downloader downloader) {
        mHandler.post(() -> {
            if (mDownloadingTaskList.containsKey(key)) {
                mDownloadingTaskList.remove(key);
            }
        });
    }

}
