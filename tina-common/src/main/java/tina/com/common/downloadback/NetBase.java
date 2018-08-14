package tina.com.common.downloadback;

import io.reactivex.disposables.Disposable;

/**
 * @author: 小民
 * @date: 2017-06-10
 * @time: 11:17
 * @开源地址: https://github.com/2745329043/XDownloadDemo
 * @说明: 网络请求
 */
public interface NetBase {
    /** 网络请求 加入队列与清除 -> 主要针对资源回收 */
    boolean addRequestQueue(Disposable disposable);
    void removeRequestQueue(Disposable disposable);
    /** 网络资源回收 */
    void netGc();
}
