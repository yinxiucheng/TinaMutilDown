package tina.com.common.http.net.gson;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author Ragnar
 * @date 2018/5/14 下午3:36
 */
@SuppressWarnings("unused")
public class GsonUtil {

    private static Gson instance = new Gson();

    public static <T> T getObjectFromJson(String gson, Class<T> t) {
        if (TextUtils.isEmpty(gson)) return null;
        return instance.fromJson(gson, t);
    }

    public static <T> ArrayList<T> getObjectFromJson(String gson,Type type) {
        if (TextUtils.isEmpty(gson)) return null;
        return instance.fromJson(gson, type);
    }

    public static <T> String toJson(T t) {
        return instance.toJson(t);
    }

}
