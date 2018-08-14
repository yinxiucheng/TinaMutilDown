package tina.com.common.http.config;

import tina.com.common.http.sdk.LiveHostType;

/**
 * @author Ragnar
 * @date 2018/5/10 下午6:59
 * @description 网络url相关配置
 */
public class UrlConfig {

    private static LiveHostType currentHostType;

    private static final String API_PRE = "http://pre-api.voice.mu.com";
    private static final String API_BETA = "http://beta-api.voice.mu.com";
    private static final String API_ONLINE = "http://api.voice.mu.com";

    private static final String IM_SERVER_PRE = "http://pre.im.voice.mu.com";
    private static final String IM_SERVER_BETA = "http://betaim.voice.mu.com";
    private static final String IM_SERVER_ONLINE = "http://im.voice.mu.com";

    private static final String WEB_H5_BASE_URL_PRE = "http://pre-www2.mpa.com/";
    private static final String WEB_H5_BASE_URL_BETA = "http://beta-www2.mpa.com/";
    private static final String WEB_H5_BASE_URL_ONLINE = "https://www2.mpa.com/";

    public static  String CURRENT_API = API_ONLINE;
    public static  String CURRENT_IM_SERVER = IM_SERVER_ONLINE;
    public static  String CURRENT_H5_URL = WEB_H5_BASE_URL_ONLINE;

    public static final String HEADER_URLNAME_KEY = "urlname";

    //处理其它的不同的baseurl的情况，需要添加header参数
    public static final String HEADER_DOMAIN_API_VALUE = "voice.mu.com";
    public static final String HEADER_DOMAIN_IM_VALUE = "im.voice.mu.com";
    public static final String HEADER_DOMAIN_H5_VALUE = "www2.mpa.com";

    public static void setHostType(LiveHostType hostType) {
        currentHostType = hostType;
        switch (hostType) {
            case PRE:
                CURRENT_API = API_PRE;
                CURRENT_IM_SERVER = IM_SERVER_PRE;
                CURRENT_H5_URL = WEB_H5_BASE_URL_PRE;
                break;
            case BETA:
                CURRENT_API = API_BETA;
                CURRENT_IM_SERVER = IM_SERVER_BETA;
                CURRENT_H5_URL = WEB_H5_BASE_URL_BETA;
                break;
            case ONLINE:
                CURRENT_API = API_ONLINE;
                CURRENT_IM_SERVER = IM_SERVER_ONLINE;
                CURRENT_H5_URL = WEB_H5_BASE_URL_ONLINE;
                break;
        }
    }
    public static boolean isTestAPIEnvironment() {
        return currentHostType != LiveHostType.ONLINE;
    }
}
