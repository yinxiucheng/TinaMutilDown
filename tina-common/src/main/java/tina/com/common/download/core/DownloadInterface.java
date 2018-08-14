package tina.com.common.download.core;

import tina.com.common.download.DownloadException;

/**
 * @author yxc
 * @date 2018/8/7
 */
public interface DownloadInterface {

    interface OnDownloadListener {

//        void onDownloadConnecting();

        void onDownloadProgress(long finished, long length);

        void onDownloadCompleted();

        void onDownloadPaused();

        void onDownloadCanceled();

        void onDownloadFailed(DownloadException de);
    }

    void pause();

    void cancel();

    boolean isDownloading();

    boolean isComplete();

    boolean isPaused();

    boolean isCanceled();

    boolean isFailed();

}
