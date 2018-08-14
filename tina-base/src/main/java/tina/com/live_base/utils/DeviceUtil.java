package tina.com.live_base.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * @author Ragnar
 * @date 2018/5/16 下午4:27
 */
@SuppressWarnings("unused")
public class DeviceUtil {

    public static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    public static int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels - getStatusBarHeight();

    /**
     * @date 2018/6/5
     * @description 获取屏幕显示高度
     */
    public static int getScreenHeight() {
        return screenWidth;
    }


    /**
     * 获取屏幕密度值
     * @return 屏幕密度值
     */
    public static float getDensityValue() {
        return getDensityValue(BaseUtils.getContext());
    }

    /**
     * 获取屏幕密度值
     * @return 屏幕密度值
     */
    public static float getDensityValue(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.density;
    }


    /**
     * @date 2018/6/5
     * @description 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        return resourceId > 0 ? Resources.getSystem().getDimensionPixelSize(resourceId) : 0;
    }

    /**
     * @date 2018/6/5
     * @description 获取虚拟按键高度
     */
    @SuppressWarnings("unchecked")
    public static int getControlBarHeight() {
        try {
            WindowManager windowManager = (WindowManager) BaseUtils.getContext().getSystemService(Context.WINDOW_SERVICE);
            Class clazz = Class.forName("android.view.Display");
            Method method = clazz.getMethod("getRealMetrics", DisplayMetrics.class);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            if (windowManager != null) {
                method.invoke(windowManager.getDefaultDisplay(), displayMetrics);
            }
            return displayMetrics.heightPixels - Resources.getSystem().getDisplayMetrics().heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isNavigationBarShow(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = context.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            boolean result = realSize.y != size.y;
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !(menu || back);
        }
    }

    public static int uiPX(int uiPT) {
        return (int) (uiPT * 1.018f * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取设备的语言
     */
    public static String getLanguage() {
        String resultLanguage = "";
        String language = Locale.getDefault().getLanguage();
        if (!TextUtils.isEmpty(language)) {
            if ("zh".equals(language)) {
                String country = Locale.getDefault().getCountry();
                resultLanguage = "zh-Hans";
                if (!TextUtils.isEmpty(country)
                        && !(country.equalsIgnoreCase("CN") || country.equalsIgnoreCase("CHN"))) {
                    resultLanguage = "zh-Hant";
                }
            } else {
                resultLanguage = language;
            }
        }
        return resultLanguage;
    }

    private static String deviceId = null;

    public static String getDeviceId() {
        if (deviceId == null) {
            @SuppressLint("HardwareIds") String serial = Build.SERIAL;
            @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(BaseUtils.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                    Build.BOARD.length() % 10 +
                    Build.BRAND.length() % 10 +
                    Build.CPU_ABI.length() % 10 +
                    Build.DEVICE.length() % 10 +
                    Build.DISPLAY.length() % 10 +
                    Build.HOST.length() % 10 +
                    Build.ID.length() % 10 +
                    Build.MANUFACTURER.length() % 10 +
                    Build.MODEL.length() % 10 +
                    Build.PRODUCT.length() % 10 +
                    Build.TAGS.length() % 10 +
                    Build.TYPE.length() % 10 +
                    Build.USER.length() % 10;
            String m_szLongID = serial + m_szDevIDShort
                    + androidId;
            MessageDigest m = null;
            try {
                m = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if (m != null) {
                m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
                // get md5 bytes
                byte p_md5Data[] = m.digest();
                // create a hex string
                StringBuilder m_szUniqueID = new StringBuilder();
                for (byte aP_md5Data : p_md5Data) {
                    int b = (0xFF & aP_md5Data);
                    // if it is a single digit, make sure it have 0 in front (proper padding)
                    if (b <= 0xF) {
                        m_szUniqueID.append("0");
                    }
                    // add number to string
                    m_szUniqueID.append(Integer.toHexString(b));
                }
                // hex string to uppercase
                deviceId = m_szUniqueID.toString().toUpperCase();
            }
            return deviceId;
        } else
            return deviceId;
    }

    /***
     *  获取设备的IMEI值
     *  需要权限 {@link Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getIMEIValue() {
        String deviceId = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) BaseUtils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) deviceId = telephonyManager.getDeviceId();
        } catch (Exception exception) {
            LogUtil.d(exception.toString());
        }
        return deviceId;
    }

    /***
     *  获得手机MAC值
     *  需要权限 {@link Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     */
    @SuppressLint("HardwareIds")
    public static String getMacValue() {
        String macValue = null;
        try {
            // WifiManager获得方式在开机后没有打开过wifi，就无法获得mac
            @SuppressLint("WifiManagerLeak")
            WifiManager wifi = (WifiManager) BaseUtils.getContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi != null ? wifi.getConnectionInfo() : null;
            macValue = info != null ? info.getMacAddress() : null;
        } catch (Exception e) {
            LogUtil.d(e.toString());
        }

        if (TextUtils.isEmpty(macValue)) {
            try {
                String str = "";
                // 该方式在MX、root的小米上均无法获得，在联想S720上可以
                Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
                InputStreamReader ir = new InputStreamReader(pp.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);

                for (; null != str; ) {
                    str = input.readLine();
                    if (str != null) {
                        macValue = str.trim();// 去空格
                        break;
                    }
                }
            } catch (IOException ex) {
                // 赋予默认值
            }
        }
        return macValue;
    }

    /**
     * 获取 SIM 唯一标识码
     * 需要权限 {@link Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     */
    public static String getIccId(@NonNull Context context) {
        String iccId = "";
        try {
            boolean hasPermission = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
            if (!hasPermission) {
                return iccId;
            }
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("HardwareIds")
            String number = telephonyManager != null ? telephonyManager.getSimSerialNumber() : null;
            if (!TextUtils.isEmpty(number)) {
                iccId = number;
            }
            return iccId;
        } catch (Exception e) {
            return iccId;
        }
    }

    /**
     * 获取 Android ID
     */
    public static String getAndroidId(@NonNull Context context) {
        String androidId = Settings.System.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return null == androidId ? "" : androidId;
    }


    /**
     * 获得屏幕高度
     */
    public static int getScreenWidth() {
        return screenWidth;
    }

    /**
     * 获得屏幕宽度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

}
