package tina.com.common.downloadback.common;

/**
 * @author: 小民
 * @date: 2017-06-06
 * @time: 20:23
 * @开源地址: https://github.backcom/2745329043/XDownloadDemo
 * @说明: 网络请求过程中的进度
 */
public interface NetProgressListener {
    /**
     * 下载进度
     * @param url             下载地址
     * @param readCount      读取字节
     * @param totalCount     总字节
     * @param done           是否下载完成
     */
    void update(String url, long readCount, long totalCount, boolean done);
}
