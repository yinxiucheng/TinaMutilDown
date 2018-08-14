package tina.com.common.http.net.interceptor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.annimon.stream.Stream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import tina.com.common.http.net.params.CommonParams;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author yxc
 * @date 2018/7/16
 */
public class CommonParamsInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取到request
        Request request = chain.request();
        //获取到方法
        String method = request.method();
        Uri uri = Uri.parse(request.url().toString());
        Map<String, String> paramsMap = parseParams(request, uri);
        Request.Builder builder = request.newBuilder();
        initHeaderData(builder);
        initBaseParams(builder, request, uri.getHost(), uri.getPath().substring(1, uri.getPath().length()), paramsMap);
        //最后通过chain.proceed(request)进行返回
        return chain.proceed(builder.build());
    }

    @NonNull
    private Map<String, String> parseParams(Request request, Uri uri) throws IOException {
        Map<String, String> paramsMap = new HashMap<>();
        RequestBody requestBody = request.body();
        if (requestBody != null && requestBody.contentLength() > 0) {
            if (requestBody instanceof FormBody) {
                FormBody formBody = (FormBody) requestBody;
                for (int i = 0; i < formBody.size(); i++) {
                    paramsMap.put(formBody.name(i), formBody.value(i));
                }
            }
        } else {
            String query = uri.getQuery();
            if (!TextUtils.isEmpty(query)) {
                String[] splitArray = query.split("&");
                for (String str : splitArray) {
                    String[] splitParam = str.split("=");
                    if (splitParam.length == 2) {
                        paramsMap.put(splitParam[0], splitParam[1]);
                    }
                }
            }
        }
        return paramsMap;
    }


    private synchronized void initHeaderData(Request.Builder builder) {
        CommonParams.initHeaderParams(builder);
    }

    private synchronized void initBaseParams(Request.Builder builder, Request request, String host,
                                             String path, Map<String, String> paramsMap) {
        Map<String, String> params = CommonParams.getBaseParams(host, path, paramsMap);
        if ("POST".equals(request.method())) {
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            Stream.of(params).forEach(entry -> {
                        if (!TextUtils.isEmpty(entry.getValue()))
                            formBodyBuilder.add(entry.getKey(), entry.getValue());
                    }
            );
            builder.post(formBodyBuilder.build());
        } else if ("GET".equals(request.method())) {
            HttpUrl.Builder urlBuilder = request.url().newBuilder();
            Stream.of(params).forEach(entry -> {
                        if (!TextUtils.isEmpty(entry.getValue()) && !paramsMap.containsKey(entry.getKey()))
                            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                    }
            );
            builder.url(urlBuilder.build());
        }
    }

}
