package tina.com.common.http.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import tina.com.common.http.net.ResponseCode;

/**
 * Created by Allen on 2017/10/23.
 *
 * @author Allen
 * <p>
 * 返回数据基类
 */

public class BaseData<T> implements Serializable {

    private static final long serialVersionUID = -3609488127407956464L;

    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    protected String message;
    @SerializedName("data")
    private T data;

    public T getData() {
        return data;
    }

    public ResponseCode getCode() {
        return ResponseCode.codeNumOf(code);
    }

    public String getMessage() {
        return message;
    }


    @Override
    public String toString() {
        return "BaseData{" +
                "code=" + code +
                ", msg='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
