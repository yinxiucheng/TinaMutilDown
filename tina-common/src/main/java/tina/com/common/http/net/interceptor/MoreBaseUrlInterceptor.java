package tina.com.common.http.net.interceptor;

import android.webkit.URLUtil;

import java.io.IOException;
import java.util.List;
import tina.com.common.http.config.UrlConfig;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author yxc
 * @date 2018/7/16
 */
public class MoreBaseUrlInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取原始的originalRequest
        Request originalRequest = chain.request();
        //获取老的url
        HttpUrl oldUrl = originalRequest.url();
        //获取originalRequest的创建者builder
        Request.Builder builder = originalRequest.newBuilder();
        //获取头信息的集合如：manage,mdffx
        List<String> urlnameList = originalRequest.headers(UrlConfig.HEADER_URLNAME_KEY);
        if (urlnameList != null && urlnameList.size() > 0) {
            //删除原有配置中的值,就是namesAndValues集合里的值
            builder.removeHeader(UrlConfig.HEADER_URLNAME_KEY);
            //获取头信息中配置的value,如：manage或者mdffx
            String urlname = urlnameList.get(0);
            HttpUrl baseURL = null;
            //根据头信息中配置的value,来匹配新的base_url地址
            if (UrlConfig.HEADER_DOMAIN_IM_VALUE.equals(urlname)) {
                baseURL = HttpUrl.parse(UrlConfig.CURRENT_IM_SERVER);
            } else if (UrlConfig.HEADER_DOMAIN_H5_VALUE.equals(urlname)) {
                baseURL = HttpUrl.parse(UrlConfig.CURRENT_H5_URL);
            } else if (URLUtil.isHttpUrl(urlname) || URLUtil.isHttpsUrl(urlname)) {
                baseURL = HttpUrl.parse(urlname);
            } else {
                baseURL = HttpUrl.parse(UrlConfig.CURRENT_API);
            }
            //重建新的HttpUrl，需要重新设置的url部分
            HttpUrl newHttpUrl = oldUrl.newBuilder()
                    .scheme(baseURL.scheme())//http协议如：http或者https
                    .host(baseURL.host())//主机地址
                    .port(baseURL.port())//端口
                    .build();
            //获取处理后的新newRequest
            Request newRequest = builder.url(newHttpUrl).build();
            return chain.proceed(newRequest);
        } else {
            return chain.proceed(originalRequest);
        }
    }
}
