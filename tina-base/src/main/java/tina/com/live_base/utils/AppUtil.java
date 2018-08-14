package tina.com.live_base.utils;

import java.util.Locale;

/**
 * @author mtb0074.
 * @since 13-12-23.
 * 与app相关工具函数，如快捷方式创建等.
 */
public class AppUtil {

    /**
     * 判断当前的系统是否为简体中文环境
     *
     * @return
     */
    public static final boolean isSimpleChineseSystem() {
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        String country = locale.getCountry();
        if (language != null && "zh".equalsIgnoreCase(language)) {
            if ("cn".equalsIgnoreCase(country)) {
                return true;
            }
        }
        return false;
    }
}
