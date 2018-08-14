package tina.com.common.downloadback;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import tina.com.common.download.utils.NetworkUtil;
import tina.com.common.download.utils.UnifiedErrorUtil;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import tina.com.common.downloadback.buffer.BufferDbUtil;
import tina.com.common.downloadback.buffer.DownInfoDbUtil;
import tina.com.common.downloadback.common.NetProgressInterceptor;
import tina.com.common.downloadback.common.NetProgressListener;

/**
 * @author: 小民
 * @date: 2017-06-05
 * @time: 16:33
 * @开源地址: https://github.com/2745329043/XDownloadDemo
 * @说明: Retrofit 网络请求操作器
 */
public class DownloadRetrofitClient {
    //Buid 构造参数
    private Builder mBuilder;
    //当前正在下载的队列数量
    private int mCurrentDownloadCount;
    //网络请求
    private static HttpDownService mHttpDownService;
    /* Retrofit */
    private static DownloadRetrofitClient mRetrofitClient;
    /* 网络 ->  为了用静态，避免资源回收 */
    private static List<NetProgressListener> mNetProgressListeners = new ArrayList<>();

    /**
     * 构造函数
     */
    public DownloadRetrofitClient(Builder builder) {
        mBuilder = builder;
    }

    /**
     * 下载功能
     */
    public static HttpDownService getDownService() {
        if (mHttpDownService == null) {
            mHttpDownService = mRetrofitClient.crateRetrofit().create(HttpDownService.class);
        }
        return mHttpDownService;
    }

    /**
     * 创建Retroft
     *
     * @return
     */
    private Retrofit crateRetrofit() {
        DownloadRetrofitClient.log("创建RetrofitClient");
        /* 拦截器 ->  */
        Interceptor interceptor = new NetProgressInterceptor(new NetProgressListener() {
            @Override
            public void update(String url, long readCount, long totalCount, boolean done) {
                for (int i = 0; i < mNetProgressListeners.size(); i++) {
                    mNetProgressListeners.get(i).update(url, readCount, totalCount, done);
                }
            }
        });
        //初始化OkHttp
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(mBuilder.Down_Connection_Time, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        //初始化 retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .baseUrl(mBuilder.baseUrl)
                .build();
        return retrofit;
    }

    /**
     * 获取控制器
     */
    public static DownloadRetrofitClient client() {
        return mRetrofitClient;
    }

    /**
     * 输出日志
     */
    public static void log(String message) {
        if (client().mBuilder.debug) {
            Log.e("HttpLog", message);
        }
    }

    /**
     * 为OkHttpClient 新增 interceptors
     */
    public void addNetProgressListener(NetProgressListener listener) {
        mNetProgressListeners.add(listener);
    }

    /**
     * 为OkHttpClient 移除 interceptors
     */
    public void removeNetProgressListener(NetProgressListener listener) {
        mNetProgressListeners.remove(listener);
    }

    /**
     * 当前正在下载的任务
     */
    public int getCurrentDownloadCount() {
        return mCurrentDownloadCount;
    }

    /**
     * 最大下载队列
     */
    public int getMaxDownloadCount() {
        return mBuilder.downloadsQueueCount;
    }

    /**
     * 新增下载队列  +1
     */
    public void addCurrentDownloadCount() {
        log("新增下载队列 当前队列数：" + mCurrentDownloadCount);
        mCurrentDownloadCount++;
    }

    /**
     * 移除下载队列 -1
     */
    public void subCurrentDownloadCount() {
        log("移除下载队列 当前队列数：" + mCurrentDownloadCount);
        mCurrentDownloadCount--;
    }

    /**
     * 初始化Buider
     */
    public static Builder init(Context context) {
        return new Builder(context);
    }

    /**
     * Buider构造
     */
    public static final class Builder {
        Context context;
        //base
        String baseUrl;
        //主工程的applictiond
        String applictionId;
        //下载队列。默认最大5
        int downloadsQueueCount = 5;
        //是否开启调试输出
        boolean debug;
        /* 缓冲数据库,表名 */
        String dbName = "net_buffer_db";
        /*超时超时-默认6秒*/
        int Connection_Timeout = 6;
        /*读取超时-默认6秒*/
        int Read_Timeout = 6;
        /*下载超时-默认30秒*/
        int Down_Connection_Time = 30;
        /*有网情况下的本地缓存时间默认60秒*/
        int Net_Buffer_Time = 60;
        /*无网络的情况下本地缓存时间默认7天*/
        int No_Net_Buffer_Time = 24 * 60 * 60 * 7;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 接口 baseUrl
         *
         * @param baseUrl
         * @return
         */
        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * 设置主工程 appID，用于7.0 安装apk权限
         *
         * @param applictionId
         * @return
         */
        public Builder setApplictionId(String applictionId) {
            this.applictionId = applictionId;
            return this;
        }

        /**
         * 设置下载队列，最大下载次数
         *
         * @param downloadsQueueCount
         * @return
         */
        public Builder setDownloadsQueueCount(int downloadsQueueCount) {
            this.downloadsQueueCount = downloadsQueueCount;
            return this;
        }

        /**
         * 是否开启网络请求输出 调试日志
         *
         * @param debug
         */
        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        /**
         * 缓冲数据库,表名
         */
        public Builder setDbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        /**
         * 超时超时-默认6秒
         */
        public Builder setConnectionTimeout(int connection_Timeout) {
            Connection_Timeout = connection_Timeout;
            return this;
        }

        /**
         * 读取超时-默认6秒
         */
        public Builder setReadTimeout(int read_Timeout) {
            Read_Timeout = read_Timeout;
            return this;
        }

        /**
         * 下载连接超时-默认30秒
         */
        public Builder setDownConnectionTime(int down_Connection_Time) {
            Down_Connection_Time = down_Connection_Time;
            return this;
        }

        /**
         * 有网情况下的本地缓存时间默认60秒
         */
        public Builder setNetBufferTime(int net_Buffer_Time) {
            Net_Buffer_Time = net_Buffer_Time;
            return this;
        }

        /**
         * 无网络的情况下本地缓存时间默认7天
         */
        public Builder setNoNetBufferTime(int no_Net_Buffer_Time) {
            No_Net_Buffer_Time = no_Net_Buffer_Time;
            return this;
        }


        /**
         * 初始化完成
         */
        public void build() {
            //检测参数
            if (context == null || applictionId == null) {
                Log.e("tag", "警告：参数配置异常");
                return;
            }
            //初始化Retrofit
            mRetrofitClient = new DownloadRetrofitClient(this);
            //Toast
//            ToastUtils.init(context);
            //文件操作
            NetworkUtil.init(context, applictionId);
            //初始化数据 -> 用户网络缓存
            BufferDbUtil.init(context, dbName);
            //初始化数据 -> 用户网络缓存
            DownInfoDbUtil.init(context, dbName);
            //网络异常 统一提示语，初始化 -> 因为里面 要 getResouce
            UnifiedErrorUtil.init(context);
        }
    }

    /**
     * 有网情况下的本地缓存时间默认60秒
     */
    public int getNetBufferTime() {
        return mBuilder.Net_Buffer_Time;
    }

    /**
     * 无网络的情况下本地缓存时间默认7天
     */
    public int getNoNetBufferTime() {
        return mBuilder.No_Net_Buffer_Time;
    }
}
