package tina.com.live_base.account;

import android.text.TextUtils;

import tina.com.live_base.sp.SharedKey;
import tina.com.live_base.sp.SharedPreferencesUtil;


/**
 * @author Ragnar
 * @date 2018/5/25
 * @description 用户信息维护
 */
public class LiveAccountManager {

    private static OauthModel oauthInfo;

    public static void saveOauthInfo(OauthModel bean) {
        oauthInfo = bean;
        if (bean == null) {
            SharedPreferencesUtil.saveData(SharedKey.OAUTH_INFO, null);
        } else {
            SharedPreferencesUtil.saveData(SharedKey.OAUTH_INFO, bean);
        }
    }

    public static boolean isLogin() {
        return oauthInfo != null && !TextUtils.isEmpty(oauthInfo.getAccess_token());
    }

    public static OauthModel getOauthInfo() {
        if (oauthInfo != null) return oauthInfo;
        return oauthInfo = SharedPreferencesUtil.getObject(SharedKey.OAUTH_INFO, OauthModel.class);
    }

    public static int getLoginUserId() {
        OauthModel oauthInfo = getOauthInfo();
        if (oauthInfo != null && oauthInfo.getUser() != null) return oauthInfo.getUser().getId();
        return -1;
    }

    public static UserModel getLoginUser() {
        OauthModel oauthInfo = getOauthInfo();
        if (oauthInfo != null) return oauthInfo.getUser();
        return null;
    }

    public static String getAccessToken() {
        OauthModel oauthInfo = getOauthInfo();
        if (oauthInfo != null) return oauthInfo.getAccess_token();
        return null;
    }

    public static void logout() {
        saveOauthInfo(null);
    }

    /**
     * 判断当前保存的token是否有效
     */
    public static boolean isSessionValid(OauthModel oauthBean) {
        return oauthBean != null && !TextUtils.isEmpty(oauthBean.getAccess_token());
    }

}
