package tina.com.common.http.net;

import com.annimon.stream.Stream;

public enum ResponseCode {
    NETWORK_ERROR(-1), // 网络异常
    SUCCESS(100000), // 成功
    FAILED(100001), // 失败（接口调用失败默认返回值）
    NO_LOGIN(100002), // 未登录
    USER_FORBIDDEN(100003), // 用户禁用
    USER_LEVEL_LIMIT(100004), // 用户等级限制

    // im评论相关
    IM_SUCCESS(0), // im 成功
    IM_COMMENT_GLOBAL_SPEAKING_NOT_ALLOW(3), // 评论被全局禁言
    IM_COMMENT_AUTHOR_SPEAKING_NOT_ALLOW(4), // 评论被某个直播禁言
    IM_COMMENT_LIVE_SPEAKING_NOT_ALLOW(5), // 评论某个主播禁言

    OTHER(-10000); // 其它

    public int code;

    ResponseCode(int code) {
        this.code = code;
    }

    private ResponseCode setCode(int code) {
        this.code = code;
        return this;
    }

    public static ResponseCode codeNumOf(int codeNum) {
        return Stream.of(ResponseCode.values()).filter(p -> p.code == codeNum).findFirst()
                .orElseGet(() -> ResponseCode.OTHER.setCode(codeNum));
    }



}