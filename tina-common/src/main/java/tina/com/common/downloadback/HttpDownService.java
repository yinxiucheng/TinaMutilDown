package tina.com.common.downloadback;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author: 小民
 * @date: 2017-06-08
 * @time: 20:58
 * @开源地址: https://github.com/2745329043/XDownloadDemo
 * @说明: 下载文件专用
 */
public interface HttpDownService {
    /*断点续传下载接口*/
    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);
}
