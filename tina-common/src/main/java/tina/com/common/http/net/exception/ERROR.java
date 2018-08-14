package tina.com.common.http.net.exception;

/**
 * @author yxc
 * @date 2018/7/18
 *
 * 约定异常
 */
public final class ERROR {
    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;
    /**
     * 连接超时
     */
    public static final int TIMEOUT_ERROR = 1001;
    /**
     * 空指针错误
     */
    public static final int NULL_POINTER_EXCEPTION = 1002;

    /**
     * 证书出错
     */
    public static final int SSL_ERROR = 1003;

    /**
     * 类转换错误
     */
    public static final int CAST_ERROR = 1004;

    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1005;

    /**
     * 非法数据异常
     */
    public static final int ILLEGAL_STATE_ERROR = 1006;
}
