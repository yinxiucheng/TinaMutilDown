package tina.com.common.download.core;

import android.os.Process;
import android.text.TextUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import tina.com.common.download.DownloadException;
import tina.com.common.download.entity.DownloadStatus;
import tina.com.common.download.utils.Constants;
import tina.com.common.download.utils.DownloadConfig;
import tina.com.common.download.utils.DownloadUtils;
import tina.com.common.download.utils.Trace;

/**
 * @author yxc
 * @date 2018/8/6
 */
public class ConnectThread implements Runnable, ConnectInterface {

    private String mUrl;

    private OnConnectListener mOnConnectListener;

    private volatile int mStatus;

    private volatile boolean isRunning;

    public ConnectThread(String url, OnConnectListener listener) {
        this.mUrl = url;
        this.mOnConnectListener = listener;
    }

    @Override
    public void pause() {
        mStatus = DownloadStatus.PAUSED;
    }

    public void cancel() {
        mStatus = DownloadStatus.CANCELED;
    }

    @Override
    public boolean isConnecting() {
        return mStatus == DownloadStatus.CONNECTING;
    }

    @Override
    public boolean isConnected() {
        return mStatus == DownloadStatus.CONNECTED;
    }

    @Override
    public boolean isPaused() {
        return mStatus == DownloadStatus.PAUSED;
    }

    @Override
    public boolean isCanceled() {
        return mStatus == DownloadStatus.CANCELED;
    }

    @Override
    public boolean isFailed() {
        return mStatus == DownloadStatus.FAILED;
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        mStatus = DownloadStatus.CONNECTING;
//        mOnConnectListener.onConnecting();
        try {
            executeConnection();
        } catch (DownloadException e) {
            Trace.d("HttpURLConnection: exception " + e.toString());
            handleDownloadException(e);
        }
    }

    private void executeConnection() throws DownloadException {
        isRunning = true;
        URL url;
        try {
            url = new URL(mUrl);
        } catch (MalformedURLException e) {
            throw new DownloadException(DownloadStatus.FAILED, "Bad url.", e);
        }
        excuteConnectionInternal(url);
    }

    private void excuteConnectionInternal(URL url) throws DownloadException {
        HttpURLConnection httpConnection = null;
        try {
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(Constants.CONNECT_TIME_OUT);
            httpConnection.setReadTimeout(Constants.READ_TIME_OUT);
            httpConnection.setRequestMethod(Constants.GET);
            httpConnection.setRequestProperty("Range", "bytes=" + 0 + "-");
            int responseCode = httpConnection.getResponseCode();
            Trace.d("HttpURLConnection:reponseCode " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                parseResponse(httpConnection, false);
            } else if (responseCode == HttpURLConnection.HTTP_PARTIAL) {
                parseResponse(httpConnection, true);
            } else if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                parseRedirect(httpConnection);
            } else {
                throw new DownloadException(DownloadStatus.FAILED, "UnSupported response code:" + responseCode);
            }
            isRunning = false;
        } catch (ProtocolException e) {
            isRunning = false;
            throw new DownloadException(DownloadStatus.FAILED, "Protocol error", e);
        } catch (IOException e) {
            isRunning = false;
            throw new DownloadException(DownloadStatus.FAILED, "IO error", e);
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }

    //处理重定向Url
    private void parseRedirect(HttpURLConnection httpConnection) throws DownloadException {
        String redirectUrl = httpConnection.getHeaderField("location");
        final URL url;
        try {
            url = new URL(redirectUrl);
        } catch (MalformedURLException e) {
            throw new DownloadException(DownloadStatus.FAILED, "Bad url.", e);
        }
        excuteConnectionInternal(url);
    }

    private void parseResponse(HttpURLConnection httpConnection, boolean isAcceptRanges) throws DownloadException {
        final long length;
        String contentLength = httpConnection.getHeaderField("Content-Length");
        if (TextUtils.isEmpty(contentLength) || contentLength.equals("0") || contentLength.equals("-1")) {
            length = httpConnection.getContentLength();
        } else {
            length = Long.parseLong(contentLength);
        }
        checkExternalStorageEnough(length);
        checkCanceledOrPaused();
        mStatus = DownloadStatus.CONNECTED;

        mOnConnectListener.onConnected(length, isAcceptRanges);
    }

    private void checkExternalStorageEnough(long length) throws DownloadException {
        if (length <= 0) {
            throw new DownloadException(DownloadStatus.FAILED, "length <= 0");
        } else if (DownloadConfig.getInstance().isUseExternalStorageDir() &&
                length > DownloadUtils.getEnableSize()) {
            throw new DownloadException(DownloadStatus.STATUS_STORAGE_NOT_ENOUGH, "length over external storage");
        }
    }

    private void checkCanceledOrPaused() throws DownloadException {
        if (isCanceled()) {
            // cancel
            throw new DownloadException(DownloadStatus.CANCELED, "Connection Canceled!");
        } else if (isPaused()) {
            // paused
            throw new DownloadException(DownloadStatus.PAUSED, "Connection Paused!");
        }
    }

    private void handleDownloadException(DownloadException e) {
        switch (e.getErrorCode()) {
            case DownloadStatus.STATUS_STORAGE_NOT_ENOUGH:
                synchronized (mOnConnectListener) {
                    mStatus = DownloadStatus.STATUS_STORAGE_NOT_ENOUGH;
                    mOnConnectListener.onConnectFailed(e);
                }
                break;
            case DownloadStatus.FAILED:
                synchronized (mOnConnectListener) {
                    mStatus = DownloadStatus.FAILED;
                    mOnConnectListener.onConnectFailed(e);
                }
                break;
            case DownloadStatus.PAUSED:
                synchronized (mOnConnectListener) {
                    mStatus = DownloadStatus.PAUSED;
                    mOnConnectListener.onConnectPaused();
                }
                break;
            case DownloadStatus.CANCELED:
                synchronized (mOnConnectListener) {
                    mStatus = DownloadStatus.CANCELED;
                    mOnConnectListener.onConnectCanceled();
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown state");
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}
