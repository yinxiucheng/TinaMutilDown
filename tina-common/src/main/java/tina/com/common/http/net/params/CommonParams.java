package tina.com.common.http.net.params;

import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Request;
import tina.com.common.BuildConfig;
import tina.com.common.http.sdk.SDKConfig;
import tina.com.common.http.utils.AppUtil;
import tina.com.common.http.utils.NetworkUtil;
import tina.com.live_base.account.LiveAccountManager;
import tina.com.live_base.account.OauthModel;
import tina.com.live_base.sp.SharedKey;
import tina.com.live_base.sp.SharedPreferencesUtil;
import tina.com.live_base.utils.BaseUtils;
import tina.com.live_base.utils.DeviceUtil;

/**
 * 公共参数
 *
 */
public class CommonParams {

    public static void initHeaderParams(Request.Builder builder) {
        OauthModel oauthInfo = LiveAccountManager.getOauthInfo();
        if (oauthInfo != null) builder.header("access-token", oauthInfo.getAccess_token());
    }

    public static Map<String, String> getBaseParams(String host, String path, Map<String, String> params) {
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.putAll(params);
        // 语言版本
        paramsMap.put("language", DeviceUtil.getLanguage());
        // 客户端版本
        paramsMap.put("version", AppUtil.getAppVersionCode() + "");
        // 申请应用时分配的AppKey
        paramsMap.put("sdk_client_id", SDKConfig.SDK_CLIENT_ID);
        // 当前机器唯一识别码device_id
        paramsMap.put("device_id", DeviceUtil.getDeviceId());
        // 渠道号
        paramsMap.put("channel", SharedPreferencesUtil.getString(SharedKey.APP_CHANNEL, SDKConfig.ALPHA_CHANNEL));
        // 原始渠道名
        paramsMap.put("origin_channel", SharedPreferencesUtil.getString(SharedKey.APP_CHANNEL, SDKConfig.ALPHA_CHANNEL));
        // sdk_version
        paramsMap.put("sdk_version", BuildConfig.VERSION_NAME);
        // 机型
        paramsMap.put("model", Build.MODEL);
        // 系统版本
        paramsMap.put("os", Build.VERSION.RELEASE);
        // 地区，1 中国大陆，2 港台 默认为1
        paramsMap.put("locale", "1");
        // sim卡唯一识别码。如果没sim就获取不到，可能为空
        paramsMap.put("iccid", DeviceUtil.getIccId(BaseUtils.getContext()));
        // 机器唯一识别码imei。IOS一直没传，安卓需要授权才能拿到，可能为空
        paramsMap.put("imei", DeviceUtil.getIMEIValue());
        // android系统唯一码。同一个ROM安装包的机器此值都一样
        paramsMap.put("android_id", DeviceUtil.getAndroidId(BaseUtils.getContext()));
        // 设备mac信息，安卓系统版本从6.0开始获取不到了，IOS一直都没传
        paramsMap.put("mac", DeviceUtil.getMacValue());
        // 宿主client_id，当使用组件调用相关接口服务时，需要将宿主的client_id传入，便于区分接口调用来源用于相关统计
        paramsMap.put("client_id", SharedPreferencesUtil.getString(SharedKey.HOST_CLIENT_ID, ""));
        //通过scheme透传回服务端的参数。如客户端收到mtmv://users?id=1&trunk_params=aaa，
        // 则通过这个scheme打开user个人主页(users/show)的时候，要把trunk_params做为参数一起伟上来
        paramsMap.put("trunk_params", "");
        // json结构字符串（转存到行为日志里是数组）
        paramsMap.put("ab_codes", "");
        // (V6.5+)纬度，有效范围：-90.0到+90.0，+表示北纬
        paramsMap.put("lat", SharedPreferencesUtil.getString(SharedKey.KEY_LATITUDE, ""));
        // (V6.5+)经度，有效范围：-180.0到+180.0，+表示东经
        paramsMap.put("lon", SharedPreferencesUtil.getString(SharedKey.KEY_LONGITUDE, ""));
        // (V6.7.5+)网络制式，如wifi、4G、3G...
        paramsMap.put("network", NetworkUtil.getCurrentNetworkType(BaseUtils.getContext()));
        // (V6.8.1+) 统计sdk产生的gid
        paramsMap.put("stat_gid", "");
        // (V7.3.0+) 分辨率px，格式1080*1920，宽*高
        paramsMap.put("resolution", "");

//        SigEntity sigEntity = getSignParams(host, path, paramsMap);
        // 加密串，有密钥+提交参数合成，用于服务端与客户端通信间加密验证
//        paramsMap.put("sig", sigEntity.sig);
//        // 时间变量，用于对请求参数一致时的混淆
//        paramsMap.put("sigTime", sigEntity.sigTime);
//        // 加密服务版本号
//        paramsMap.put("sigVersion", sigEntity.sigVersion);

        return paramsMap;
    }


    private static String getJsonObjectString(Map<String, String> paramsMap) {
        Set<String> keys = paramsMap.keySet();
        Iterator iterator = keys.iterator();
        JSONObject jsonObject = new JSONObject();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            String value = paramsMap.get(key);
            try {
                jsonObject.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }


//    public static String getParamsJsonString(String url, Map<String, String> paramsMap){
//        if (TextUtils.isEmpty(url)){
//            return "";
//        }
//        Uri uri = Uri.parse(url);
//        Map<String, String> map = getBaseParams(uri.getHost(), uri.getPath(), paramsMap);
//        if (null != map && map.size() > 0){
//            return getJsonObjectString(map);
//        }
//        return "";
//    }

//    private static SigEntity getSignParams(String url,
//                                           String path,
//                                           Map<String, String> paramsMap) {
//        ArrayList<String> params = new ArrayList<>();
//        OauthModel oauthInfo = LiveAccountManager.getOauthInfo();
//        if (oauthInfo != null && AppUtil.isNeedAccessToken(url) && !TextUtils.isEmpty(oauthInfo.getAccess_token())) {
//            params.add(oauthInfo.getAccess_token());
//        }
//        Stream.of(paramsMap).forEach(map -> {
//            if (!TextUtils.isEmpty(map.getValue())) params.add(map.getValue());
//        });
//        return SigEntity.generatorSigWithFinal(path,
//                params.toArray(new String[params.size()]),
//                SDKConfig.APP_SIG_ID,
//                BaseUtils.getContext());
//    }
}
