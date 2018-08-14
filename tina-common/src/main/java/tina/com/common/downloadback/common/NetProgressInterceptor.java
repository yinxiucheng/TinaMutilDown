package tina.com.common.downloadback.common;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author: 小民
 * @date: 2017-06-06
 * @time: 20:40
 * @开源地址: https://github.com/2745329043/XDownloadDemo
 * @说明:
 */
public class NetProgressInterceptor implements Interceptor {

    private NetProgressListener listener;

    public NetProgressInterceptor(NetProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response originalResponse = chain.proceed(request);
        return originalResponse.newBuilder()
                .body(new NetProgressResponseBody(request.url().toString(),originalResponse.body(), listener))
                .build();
    }
}