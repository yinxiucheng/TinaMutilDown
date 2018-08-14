package tina.com.live_base.account;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;

import tina.com.live_base.base.model.BaseModel;

@SuppressWarnings("unused")
public class OauthModel extends BaseModel implements Serializable {

    @SerializedName("error_code")
    private int errorCode;
    @SerializedName("error")
    private String error;
    @SerializedName("request")
    private String request;
    @SerializedName("error_detail")
    private String errorDetail;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    /**
     * 5.0版本以下，access_token默认有效日期为3年:旧版本的access_token维持3年有效期不变，同时旧版的access_token仅能在旧版进行相关的接口操作。
     * 5.0版本以上使用refresh_token机制，refresh_token刷新机制详见...:新版的access_token中包含用户ID，token有效期，以及用户身份状态。因此token长度需扩展，总长度大约在50~100字节以内。
     */
    private String access_token;

    /**
     * access_token仅有14天有效期，因此当access_token过期或者即将过期时(前1天内)，客户端在用户启动APP时，需根据过期时间与本地时间戳对比或者接口返回状态判定是否需要进行refresh操作。
     */
    private long expires_at;

    /**
     * refresh_token的有效期为45天
     */
    private String refresh_token;

    /**
     * refresh_token 失效时间
     */
    private long refresh_expires_at;

    private UserModel user;

    /**
     * 若返回中有need_register字段，则为初次登陆注册流程（走users/account_create接口）
     */
    private Boolean need_register;

    /**
     * 服务器建议的默认昵称， grant_type为password且为初次登陆注册流程时，服务器返回该值
     */
    private String suggested_screen_name;
    /**
     * 服务器建议的默认性别， grant_type为password且为初次登陆注册流程时，服务器返回该值
     */
    private String suggested_gender;
    /**
     * 服务器建议的头像地址
     */
    private String suggested_avatar;

    /**
     * 服务器建议的个性签名
     */
    private String suggested_description;
    private int suggested_country;
    private int suggested_province;
    private int suggested_city;

    /**
     * 注册的时候是否要绑定手机号码
     */
    private int connect_register_bind_phone;

    private Boolean result;

    /**
     * 接口返回增加need_check_auth表示需触发身份验证流程
     */
    private boolean need_check_auth;
    /**
     * 安全验证相信消息
     * { <br>
     * "msg":  "系统检测到您的账号存在安全风险，请尽快完成验证确保帐号安全。"， //客户端弹窗显示文案<br>
     * "trunk_params": ， // 调用页面时的透传参数<br>
     * } <br>
     */
    private JSONObject check_auth_detail;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(long expires_at) {
        this.expires_at = expires_at;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Boolean getNeed_register() {
        return need_register;
    }

    /**
     * 是否需要进入注册的流程
     */
    public boolean isNeedRegister() {
        return need_register != null && need_register;
    }

    public void setNeed_register(boolean need_register) {
        this.need_register = need_register;
    }

    public String getSuggested_screen_name() {
        return suggested_screen_name;
    }

    public void setSuggested_screen_name(String suggested_screen_name) {
        this.suggested_screen_name = suggested_screen_name;
    }

    public String getSuggested_gender() {
        return suggested_gender;
    }

    public void setSuggested_gender(String suggested_gender) {
        this.suggested_gender = suggested_gender;
    }

    public String getSuggested_avatar() {
        return suggested_avatar;
    }

    public void setSuggested_avatar(String suggested_avatar) {
        this.suggested_avatar = suggested_avatar;
    }

    public int getSuggested_country() {
        return suggested_country;
    }

    public void setSuggested_country(int suggested_country) {
        this.suggested_country = suggested_country;
    }

    public int getSuggested_province() {
        return suggested_province;
    }

    public void setSuggested_province(int suggested_province) {
        this.suggested_province = suggested_province;
    }

    public int getSuggested_city() {
        return suggested_city;
    }

    public void setSuggested_city(int suggested_city) {
        this.suggested_city = suggested_city;
    }

    /**
     * @return the suggested_description
     */
    public String getSuggested_description() {
        return suggested_description;
    }

    /**
     * @param suggested_description the suggested_description to set
     */
    public void setSuggested_description(String suggested_description) {
        this.suggested_description = suggested_description;
    }

    public int getConnect_register_bind_phone() {
        return connect_register_bind_phone;
    }

    public void setConnect_register_bind_phone(int connect_register_bind_phone) {
        this.connect_register_bind_phone = connect_register_bind_phone;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public long getRefresh_expires_at() {
        return refresh_expires_at;
    }

    public void setRefresh_expires_at(long refresh_expires_at) {
        this.refresh_expires_at = refresh_expires_at;
    }

    public boolean isNeed_check_auth() {
        return need_check_auth;
    }

    public void setNeed_check_auth(boolean need_check_auth) {
        this.need_check_auth = need_check_auth;
    }

    /**
     * 安全验证相信消息
     * { <br>
     * "msg":  "系统检测到您的账号存在安全风险，请尽快完成验证确保帐号安全。"， //客户端弹窗显示文案<br>
     * "trunk_params": ， // 调用页面时的透传参数<br>
     * } <br>
     */
    public JSONObject getCheck_auth_detail() {
        return check_auth_detail;
    }

    public void setCheck_auth_detail(JSONObject check_auth_detail) {
        this.check_auth_detail = check_auth_detail;
    }

    @Override
    public String toString() {
        return "OauthBean{" +
                "access_token='" + access_token + '\'' +
                ", expires_at=" + expires_at +
                ", refresh_token='" + refresh_token + '\'' +
                ", refresh_expires_at=" + refresh_expires_at +
                '}';
    }
}
