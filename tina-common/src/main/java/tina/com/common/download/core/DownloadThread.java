package tina.com.common.download.core;

import android.os.Process;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import tina.com.common.download.DownloadException;
import tina.com.common.download.data.DBHelper;
import tina.com.common.download.entity.DownloadInfo;
import tina.com.common.download.entity.DownloadStatus;
import tina.com.common.download.entity.ThreadInfo;
import tina.com.common.download.utils.Constants;
import tina.com.common.download.utils.DownloadConfig;
import tina.com.common.download.utils.IOCloseUtils;

/**
 * @author yxc
 * @date 2018/8/6
 */
public abstract class DownloadThread implements Runnable, DownloadInterface{

    private DownloadInfo mDownloadInfo;
    private ThreadInfo mThreadInfo;
    OnDownloadListener mOnDownloadListener;

    private volatile int mStatus;

    //主动的操作
    private volatile int mCommend = 0;

    public DownloadThread(DownloadInfo downloadInfo, ThreadInfo threadInfo, DownloadThread.OnDownloadListener downloadListener) {
        this.mDownloadInfo = downloadInfo;
        this.mThreadInfo = threadInfo;
        this.mOnDownloadListener = downloadListener;
    }

    @Override
    public void run() {
        startDownload();
    }

    public void startDownload() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        try {
            mStatus = DownloadStatus.DOWNLOADING;
            mThreadInfo.setStatus(DownloadStatus.DOWNLOADING);
            mDownloadInfo.setStatus(DownloadStatus.DOWNLOADING);

            newOrUpdate(mThreadInfo);
            executeDownload();
            synchronized (mOnDownloadListener) {
                mStatus = DownloadStatus.COMPLETED;
                mOnDownloadListener.onDownloadCompleted();
            }
        } catch (DownloadException e) {
            handleDownloadException(e);
        }
    }

    @Override
    public void cancel() {
        mThreadInfo.setStatus(DownloadStatus.CANCELED);
        mCommend = DownloadStatus.CANCELED;
    }

    @Override
    public void pause() {
        mThreadInfo.setStatus(DownloadStatus.PAUSED);
        mCommend = DownloadStatus.PAUSED;
    }

    @Override
    public boolean isDownloading() {
        return mStatus == DownloadStatus.DOWNLOADING;
    }

    @Override
    public boolean isComplete() {
        if (mStatus == DownloadStatus.COMPLETED){
            mThreadInfo.setStatus(DownloadStatus.COMPLETED);
            newOrUpdate(mThreadInfo);
            return true;
        }
        return false;
    }

    @Override
    public boolean isPaused() {
        mThreadInfo.setStatus(DownloadStatus.PAUSED);
        return mStatus == DownloadStatus.PAUSED;
    }

    @Override
    public boolean isCanceled() {
        if (mStatus == DownloadStatus.CANCELED){
            mThreadInfo.setStatus(DownloadStatus.CANCELED);
            newOrUpdate(mThreadInfo);
            return true;
        }
        return false;
    }

    @Override
    public boolean isFailed() {
        if (mStatus == DownloadStatus.FAILED){
            mThreadInfo.setStatus(DownloadStatus.FAILED);
            newOrUpdate(mThreadInfo);
            return true;
        }
        return false;
    }

    private void handleDownloadException(DownloadException e) {
        switch (e.getErrorCode()) {
            case DownloadStatus.FAILED:
                synchronized (mOnDownloadListener) {
                    mStatus = DownloadStatus.FAILED;
                    mOnDownloadListener.onDownloadFailed(e);
                }
                break;
            case DownloadStatus.PAUSED:
                synchronized (mOnDownloadListener) {
                    mStatus = DownloadStatus.PAUSED;
                    mOnDownloadListener.onDownloadPaused();
                }
                break;
            case DownloadStatus.CANCELED:
                synchronized (mOnDownloadListener) {
                    mStatus = DownloadStatus.CANCELED;
                    mOnDownloadListener.onDownloadCanceled();
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown state");
        }
    }

    private void executeDownload() throws DownloadException {
        final URL url;
        try {
            url = new URL(mThreadInfo.getUrl());
        } catch (MalformedURLException e) {
            throw new DownloadException(DownloadStatus.FAILED, "Bad url.", e);
        }

        HttpURLConnection httpConnection = null;
        try {
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(Constants.CONNECT_TIME_OUT);
            httpConnection.setReadTimeout(Constants.READ_TIME_OUT);
            httpConnection.setRequestMethod(Constants.GET);
            setHttpHeader(getHttpHeaders(mThreadInfo), httpConnection);
            final int responseCode = httpConnection.getResponseCode();
            if (responseCode == getResponseCode()) {
                transferData(httpConnection);
            } else {
                throw new DownloadException(DownloadStatus.FAILED, "UnSupported response code:" + responseCode);
            }
        } catch (ProtocolException e) {
            throw new DownloadException(DownloadStatus.FAILED, "Protocol error", e);
        } catch (IOException e) {
            throw new DownloadException(DownloadStatus.FAILED, "IO error", e);
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }

    private void transferData(HttpURLConnection httpConnection) throws DownloadException {
        InputStream inputStream = null;
        RandomAccessFile raf = null;
        try {
            try {
                inputStream = httpConnection.getInputStream();
            } catch (IOException e) {
                throw new DownloadException(DownloadStatus.FAILED, "http get inputStream error", e);
            }
            final long offset = mThreadInfo.getStart() + mThreadInfo.getFinished();
            try {
                raf = getFile(DownloadConfig.getInstance().getDownloadDir(), mDownloadInfo.getFileName(), offset);
            } catch (IOException e) {
                throw new DownloadException(DownloadStatus.FAILED, "File error", e);
            }
            transferData(inputStream, raf);
        } finally {
            try {
                IOCloseUtils.close(inputStream);
                IOCloseUtils.close(raf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkPausedOrCanceled() throws DownloadException {
        if (mCommend == DownloadStatus.CANCELED) {
            // cancel
            throw new DownloadException(DownloadStatus.CANCELED, "Download canceled!");
        } else if (mCommend == DownloadStatus.PAUSED) {
            newOrUpdate(mThreadInfo);
            throw new DownloadException(DownloadStatus.PAUSED, "Download paused!");
        }
    }

    public boolean isRunning() {
        return mStatus == DownloadStatus.DOWNLOADING;
    }

    private void transferData(InputStream inputStream, RandomAccessFile raf) throws DownloadException {
        final byte[] buffer = new byte[1024 * 8];
        while (true) {
            checkPausedOrCanceled();
            long len = -1;
            try {
                len = inputStream.read(buffer);
                if (len == -1) {
                    break;
                }
                raf.write(buffer, 0, (int)len);
                mThreadInfo.setFinished(mThreadInfo.getFinished() + len);
                newOrUpdate(mThreadInfo);
                synchronized (mOnDownloadListener) {
                    mDownloadInfo.setFinish(mDownloadInfo.getFinish() + len);
                    mDownloadInfo.setStatus(DownloadStatus.DOWNLOADING);
                    mOnDownloadListener.onDownloadProgress(mDownloadInfo.getFinish(), mDownloadInfo.getLength());
                }
            } catch (IOException e) {
                newOrUpdate(mThreadInfo);
                throw new DownloadException(DownloadStatus.FAILED, e);
            }
        }
    }

    protected void setHttpHeader(Map<String, String> headers, URLConnection connection) {
        if (headers != null) {
            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }
        }
    }

    protected abstract Map<String, String> getHttpHeaders(ThreadInfo threadInfo);

    protected abstract RandomAccessFile getFile(File dir, String name, long offset) throws IOException;

    protected abstract int getResponseCode();

    protected abstract void newOrUpdate(ThreadInfo info);

}
