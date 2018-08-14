package tina.com.live_base.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;

import tina.com.live_base.utils.BaseUtils;
import tina.com.live_base.utils.GsonUtil;

/**
 * @author Ragnar
 * @date 2018/5/14 下午3:47
 * @description SharedPreferences cache
 */
@SuppressWarnings("unused")
public class SharedPreferencesUtil {

    private static final String sharedPreferencesName = "voice_live_data";

    public static <T> void saveData(SharedKey type, T data) {
        String key = type.name();
        SharedPreferences.Editor editor = BaseUtils.getContext()
                .getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE).edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        } else {
            editor.putString(key, GsonUtil.toJson(data));
        }
        editor.apply();
    }

    public static String getString(SharedKey type, String defaultValue) {
        return BaseUtils.getContext().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
                .getString(type.name(), defaultValue);
    }

    public static boolean getBoolean(SharedKey type, Boolean defaultValue) {
        return BaseUtils.getContext().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
                .getBoolean(type.name(), defaultValue);
    }

    public static int getInt(SharedKey type, int defaultValue) {
        return BaseUtils.getContext().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
                .getInt(type.name(), defaultValue);
    }

    public static float getFloat(SharedKey type, float defaultValue) {
        return BaseUtils.getContext().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
                .getFloat(type.name(), defaultValue);
    }

    public static long getLong(SharedKey type, long defaultValue) {
        return BaseUtils.getContext().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
                .getLong(type.name(), defaultValue);
    }

    public static <T> T getObject(SharedKey type, Class<T> t) {
        String jsonString = BaseUtils.getContext().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
                .getString(type.name(), "");
        if (!TextUtils.isEmpty(jsonString)) {
            return GsonUtil.getObjectFromJson(jsonString, t);
        }
        return null;
    }

    public static <T> ArrayList<T> getObject(SharedKey type, Type objectType) {
        String jsonString = BaseUtils.getContext().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
                .getString(type.name(), "");
        if (!TextUtils.isEmpty(jsonString)) {
            return GsonUtil.getObjectFromJson(jsonString, objectType);
        }
        return null;
    }

}
