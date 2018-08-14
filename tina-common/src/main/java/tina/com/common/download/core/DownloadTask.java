package tina.com.common.download.core;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

import tina.com.common.download.DownloadException;
import tina.com.common.download.DownloadService;
import tina.com.common.download.data.DBHelper;
import tina.com.common.download.entity.DownloadInfo;
import tina.com.common.download.entity.DownloadStatus;
import tina.com.common.download.entity.ThreadInfo;
import tina.com.common.download.utils.DownloadConfig;
import tina.com.common.download.utils.Trace;

/**
 * @author yxc
 * @date 2018/8/1
 */
public class DownloadTask implements Downloader, ConnectThread.OnConnectListener, DownloadThread.OnDownloadListener {

    DownloadInfo mDownloadInfo;

    boolean isPause;
    boolean isCancel;
    Executor mExecutor;
    Handler handler;
    Message msg;

    ConnectThread mConnectThread;

    List<ThreadInfo> threadInfos;

    private int mStatus;

    private List<DownloadThread> mDownloadThreadList;

    private OnDownloaderDestroyedListener mDestoryListener;

    public DownloadTask(DownloadInfo downloadInfo, Handler handler, Executor executor, OnDownloaderDestroyedListener destroyedListener) {
        this.mDownloadInfo = downloadInfo;
        this.handler = handler;
        this.mExecutor = executor;
        this.mDestoryListener = destroyedListener;
        init();
    }

    private void init() {
        mDownloadThreadList = new LinkedList<>();
    }

    //implement form Downloader
    @Override
    public boolean isRunning() {
        return mStatus == DownloadStatus.STARTED
                || mStatus == DownloadStatus.CONNECTING
                || mStatus == DownloadStatus.CONNECTED
                || mStatus == DownloadStatus.DOWNLOADING;
    }


    public void setStatus(int status) {
        mStatus = status;
        mDownloadInfo.status = status;
    }


    public void setStatusAndNotify(int status, int notifyStatus) {
        setStatus(status);
        notifyData(notifyStatus);
    }

    //implement form Downloader
    @Override
    public void start() {
        setStatus(DownloadStatus.STARTED);
        mConnectThread = new ConnectThread(mDownloadInfo.getUrl(), this);
        mExecutor.execute(mConnectThread);
    }

    //implement form Downloader
    @Override
    public void pause() {
        Trace.e("DownloadTask is paused");
        if (mConnectThread != null && mConnectThread.isRunning()) {
            mConnectThread.cancel();
        }
        isPause = true;
        setStatus(DownloadStatus.PAUSED);
        for (DownloadThread thread : mDownloadThreadList) {
            if (null != thread && thread.isRunning()) {
                if (mDownloadInfo.getAcceptRanges()) {
                    thread.pause();
                } else {
                    thread.cancel();
                }
            }
        }
    }

    //implement form Downloader
    @Override
    public void cancel() {
        Trace.e("DownloadTask is cancel");
        if (mConnectThread != null) {
            mConnectThread.cancel();
        }
        isCancel = true;
        setStatus(DownloadStatus.CANCELED);
        for (DownloadThread thread : mDownloadThreadList) {
            if (null != thread && thread.isRunning()) {
                thread.cancel();
            }
        }
    }

    //implement form Downloader
    @Override
    public void onDestroy() {//移除该 Task
        mDestoryListener.onDestroyed(mDownloadInfo.getUrl(), this);
    }

    public void notifyData(int notifyType) {
        msg = handler.obtainMessage();
        msg.what = notifyType;
        msg.obj = mDownloadInfo;
        handler.sendMessage(msg);
    }

    @Override
    public void onConnecting() {
        setStatusAndNotify(DownloadStatus.CONNECTED, DownloadService.NOTIFY_CONNECTING);
    }

    @Override
    public void onConnected(long length, boolean isAcceptRanges) {
        if (mConnectThread.isCanceled()) {
            // despite connection is finished, the entire downloader is canceled
            onConnectCanceled();
        } else {
            mStatus = DownloadStatus.CONNECTED;
            mDownloadInfo.setAcceptRanges(isAcceptRanges);
            mDownloadInfo.setLength(length);
            download(length, isAcceptRanges);
        }
    }

    @Override
    public void onConnectPaused() {
        onDownloadPaused();
    }

    @Override
    public void onConnectCanceled() {
        deleteFromDB();
        deleteFile();
        setStatusAndNotify(DownloadStatus.CANCELED, DownloadService.NOTIFY_PAUSE_OR_CANCEL);
        onDestroy();
    }

    @Override
    public void onConnectFailed(DownloadException de) {
        if (mConnectThread.isCanceled()) {
            onConnectCanceled();
        } else if (mConnectThread.isPaused()) {
            onDownloadPaused();
        } else {
            setStatusAndNotify(DownloadStatus.FAILED, DownloadService.NOTIFY_DOWNLOAD_ERROR);
            onDestroy();
        }
    }

    private void download(long length, boolean isSupportRange) {
        mStatus = DownloadStatus.DOWNLOADING;
        initDownloadTasks(length, isSupportRange);//启动多线程下载
        for (DownloadThread thread : mDownloadThreadList) {
            mExecutor.execute(thread);
        }
    }

    private void initDownloadTasks(long length, boolean acceptRanges) {
        mDownloadThreadList.clear();
        if (acceptRanges) {
            threadInfos = getMultiThreadInfos(length);
            // init finished
            int finished = 0;
            for (ThreadInfo threadInfo : threadInfos) {
                finished += threadInfo.getFinished();
            }
            mDownloadInfo.setFinish(finished);
            for (ThreadInfo info : threadInfos) {
                mDownloadThreadList.add(new MutileDownloadThread(mDownloadInfo, info, this));
            }
        } else {
            ThreadInfo info = getSingleThreadInfo();
            mDownloadThreadList.add(new SingleDownloadThread(mDownloadInfo, info, this));
        }
    }

    private List<ThreadInfo> getMultiThreadInfos(long length) {
        if (threadInfos != null) {
            threadInfos.clear();
        }
        threadInfos = DBHelper.getInstance().queryThreadInfoListByTag(mDownloadInfo.getTag());
        if (null == threadInfos) {
            threadInfos = new ArrayList<>();
        }

        if (threadInfos.isEmpty()) {
            final int threadNum = DownloadConfig.getInstance().getThreadNum();
            for (int i = 0; i < threadNum; i++) {
                // calculate average
                final long average = length / threadNum;
                final long start = average * i;
                final long end;
                if (i == threadNum - 1) {
                    end = length;
                } else {
                    end = start + average - 1;
                }
                ThreadInfo threadInfo = new ThreadInfo(i, mDownloadInfo.getTag(), mDownloadInfo.getUrl(), start, end, 0, 0);
                threadInfos.add(threadInfo);
            }
            mExecutor.execute(() -> {
                DBHelper.getInstance().insertThreadInfoList(threadInfos);
            });
        }
        return threadInfos;
    }

    //创建单线程下载
    private ThreadInfo getSingleThreadInfo() {
        ThreadInfo threadInfo = new ThreadInfo(0, mDownloadInfo.getTag(), mDownloadInfo.getUrl(), 0);
        return threadInfo;
    }

    @Override
    public void onDownloadProgress(long finished, long length) {
        final int percent = (int) (finished * 100 / length);
        mDownloadInfo.setProgress(percent);
        setStatusAndNotify(DownloadStatus.DOWNLOADING, DownloadService.NOTIFY_DOWNLOADING);

    }

    @Override
    public void onDownloadCompleted() {
        if (isAllComplete()) {
            deleteFromDB();
            setStatusAndNotify(DownloadStatus.COMPLETED, DownloadService.NOTIFY_COMPLETE);
            onDestroy();
        }
    }

    @Override
    public void onDownloadPaused() {
        if (isAllPaused()) {
            setStatusAndNotify(DownloadStatus.PAUSED, DownloadService.NOTIFY_PAUSE_OR_CANCEL);
            onDestroy();
        }
    }

    @Override
    public void onDownloadCanceled() {
        if (isAllCanceled()) {
            deleteFromDB();
            deleteFile();
            setStatusAndNotify(DownloadStatus.CANCELED, DownloadService.NOTIFY_PAUSE_OR_CANCEL);
        }
    }


    @Override
    public void onDownloadFailed(DownloadException e) {
        if (isAllFailed()) {
            setStatusAndNotify(DownloadStatus.FAILED, DownloadService.NOTIFY_DOWNLOAD_ERROR);
            onDestroy();
        }
    }

    private boolean isAllComplete() {
        boolean allFinished = true;
        for (DownloadThread thread : mDownloadThreadList) {
            if (!thread.isComplete()) {
                allFinished = false;
                break;
            }
        }
        return allFinished;
    }

    private boolean isAllFailed() {
        boolean allFailed = true;
        for (DownloadThread thread : mDownloadThreadList) {
            if (thread.isDownloading()) {
                allFailed = false;
                break;
            }
        }
        return allFailed;
    }

    private boolean isAllPaused() {
        boolean allPaused = true;
        for (DownloadThread thread : mDownloadThreadList) {
            if (thread.isDownloading()) {
                allPaused = false;
                break;
            }
        }
        return allPaused;
    }

    private boolean isAllCanceled() {
        boolean allCanceled = true;
        for (DownloadThread thread : mDownloadThreadList) {
            if (thread.isDownloading()) {
                allCanceled = false;
                break;
            }
        }
        return allCanceled;
    }

    private void deleteFromDB() {
//        DBHelper.getInstance().deleteThreadInfoByTag(mDownloadInfo.getTag());
//        DBHelper.getInstance().deleteDownloadInfoByTag(mDownloadInfo.getTag());
    }

    private void deleteFile() {
        File file = new File(DownloadConfig.getInstance().getDownloadDir(), mDownloadInfo.getFileName());
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

}
