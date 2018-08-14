package tina.com.common.download.core;

/**
 * @author yxc
 * @date 2018/8/1
 */
public interface DownloadListener {

    void onWaiting();

    void onStart();

    void onPause();

    void onCancel();

    void onResume();

    void onComplete();

    void onFailed();
}
