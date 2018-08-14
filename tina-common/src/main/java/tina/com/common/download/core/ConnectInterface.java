package tina.com.common.download.core;

import tina.com.common.download.DownloadException;

/**
 * @author yxc
 * @date 2018/8/7
 */
public interface ConnectInterface {

    interface OnConnectListener {

        void onConnecting();

        void onConnected(long length, boolean isAcceptRanges);

        void onConnectPaused();

        void onConnectCanceled();

        void onConnectFailed(DownloadException de);
    }

    void pause();

    void cancel();

    boolean isConnecting();

    boolean isConnected();

    boolean isPaused();

    boolean isCanceled();

    boolean isFailed();

}
