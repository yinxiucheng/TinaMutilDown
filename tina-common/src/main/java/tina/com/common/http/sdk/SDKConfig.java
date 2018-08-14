package tina.com.common.http.sdk;


import tina.com.common.BuildConfig;

/**
 * @author Ragnar
 * @date 2018/5/15 下午3:23
 * @description 语音直播sdk相关配置
 */
public class SDKConfig {

    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static final String ALPHA_CHANNEL = "alpha";

    // 申请应用时分配的AppKey SDK
    public static final String SDK_CLIENT_ID = "1189857313";

    // SigEntity使用的app_id
    public static final String APP_SIG_ID = "6362942797242326017";

    // 声网app_id
    public static final String AGORA_APP_ID = "2f6548d60847413e8371bde17c27f12c";

}
