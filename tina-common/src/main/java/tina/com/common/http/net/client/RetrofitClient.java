package tina.com.common.http.net.client;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.concurrent.TimeUnit;

import tina.com.live_base.utils.BaseUtils;
import tina.com.common.http.config.OkHttpConfig;
import tina.com.common.http.config.UrlConfig;
import tina.com.common.http.net.gson.GsonAdapter;
import tina.com.common.http.net.interceptor.CommonParamsInterceptor;
import tina.com.common.http.net.interceptor.MoreBaseUrlInterceptor;
import tina.com.common.http.net.interceptor.RxHttpLogger;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created on 2017/5/3.
 *
 * @author Allen
 * <p>
 * RetrofitClient工具类
 */

public class RetrofitClient {

    private static RetrofitClient instance;

    private Retrofit.Builder mRetrofitBuilder;

    private OkHttpClient okHttpClient;

    private String baseUrl;

    private RetrofitClient() {
        initDefaultOkHttpClient();
        baseUrl = UrlConfig.CURRENT_API;
        mRetrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonAdapter.buildGson()));
    }

    private void initDefaultOkHttpClient() {
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(BaseUtils.getContext()));
        SSLUtils.SSLParams sslParams = SSLUtils.getSslSocketFactory();

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        addInterceptor(builder);
        okHttpClient = builder.build();
    }

    private void addInterceptor(OkHttpClient.Builder build) {
        build.addInterceptor(new MoreBaseUrlInterceptor())
                .addInterceptor(new HttpLoggingInterceptor(new RxHttpLogger()))
                .addInterceptor(new CommonParamsInterceptor());
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }

    public Retrofit.Builder getRetrofitBuilder() {
        return mRetrofitBuilder;
    }

    public Retrofit getRetrofit() {
        if (null == OkHttpConfig.getOkHttpClient()) {
            return mRetrofitBuilder.client(okHttpClient).build();
        } else {
            return mRetrofitBuilder.client(OkHttpConfig.getOkHttpClient()).build();
        }
    }

}
