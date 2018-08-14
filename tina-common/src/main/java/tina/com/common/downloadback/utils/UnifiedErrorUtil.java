package tina.com.common.download.utils;

import android.content.Context;

import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import tina.com.common.R;


/**
 * @author: 小民
 * @date: 2017-06-20
 * @time: 14:06
 * @说明: 网络异常错误，统一处理
 */
public class UnifiedErrorUtil {
    private UnifiedErrorUtil(){}
    private static Context mContext;

    public static void init(Context context){
        mContext = context;
    }

    /** 统一错误处理 -> 汉化了提示，以下错误出现的情况 (ps:不一定百分百按我注释的情况，可能其他情况)*/
    public static Throwable unifiedError(Throwable e){
        Throwable throwable;
        if(e instanceof UnknownHostException) {
            //无网络的情况，或者主机挂掉了。返回，对应消息  Unable to resolve host "m.app.haosou.com": No address associated with hostname
            if (!NetworkUtil.getInstance().isNetworkAvailable()) {
                //无网络
                String hint = mContext.getResources().getString(R.string.no_internet_error_hint);
                throwable = new Throwable(hint,e.getCause());
            } else {
                //主机挂了，也就是你服务器关了
                String hint = mContext.getResources().getString(R.string.server_error_hint);
                throwable = new Throwable(hint, e.getCause());
            }
        } else if(e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof SocketException){
            //连接超时等
            String hint = mContext.getResources().getString(R.string.connect_error_hint);
            throwable = new Throwable(hint, e.getCause());
        } else if(e instanceof NumberFormatException || e instanceof IllegalArgumentException || e instanceof JsonSyntaxException){
            //也就是后台返回的数据，与你本地定义的Gson类，不一致，导致解析异常 (ps:当然这不能跟客户这么说)
            String hint = mContext.getResources().getString(R.string.parse_error_hint);
            throwable = new Throwable(hint, e.getCause());
        }else{
            //其他 未知
            String hint = mContext.getResources().getString(R.string.other_error_hint);
            throwable = new Throwable(hint, e.getCause());
        }
        return throwable;
    }
}
