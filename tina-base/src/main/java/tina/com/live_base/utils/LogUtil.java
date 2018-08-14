package tina.com.live_base.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Locale;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import tina.com.live_base.BuildConfig;


/**
 * 两种log
 * 1 普通log TAG自动产生，格式为：类名.方法名(行号)；建议普通log使用 log.i/d/....
 * 2 超级log TAG默认为LogUtils 格式为：类名.方法名.行号，并且双击可跳转，
 * 但耗能是普通log的20倍，故默认关闭打印，建议发布前关闭 log.printI/D....
 */
@SuppressWarnings("unused")
public class LogUtil {

    private static String TAG = "LogUtils";
    private static final int JSON_INDENT = 4;

    private LogUtil() {
    }

    private static boolean allow = BuildConfig.DEBUG;
    /******* 是否打印super log *********/
    private static boolean isSuperLog = BuildConfig.DEBUG;

    private static String getPrefix() {
        String prefix = "%s.%s(L:%d)";

        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        prefix = String.format(Locale.CHINA, prefix, callerClazzName, caller.getMethodName(),
                caller.getLineNumber());
        return prefix;
    }

    public static void printMethodStack() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement caller : stackTrace) {
            String callerClazzName = caller.getClassName();
            int lineNumber = caller.getLineNumber();
            String methodName = caller.getMethodName();
            i("methodName=" + methodName + " callerClazzName=" + callerClazzName + " lineNumber=" + lineNumber);
        }
    }

    /**
     * Send a DEBUG log message,蓝色
     */
    public static void d(String content) {
        if (allow) {
            Log.d(getPrefix(), content);
        }
    }

    /**
     * Send an ERROR log message,红色
     */
    public static void e(String content) {
        if (allow) {
            Log.e(getPrefix(), content + "");
        }
    }

    /**
     * Send an ERROR log message,红色
     * 用完记得删除,会在公版打log
     */
    public static void releaseE(String content) {
        Log.e(getPrefix(), content + "");
    }

    /**
     * Send an INFO log message,绿色
     */
    public static void i(String content) {
        if (allow) {
            Log.i(getPrefix(), content);
        }
    }

    private static long lastTime;

    public static void measureTime(String content, long time) {
        if (lastTime == 0) {
            content = content + time;
        } else {
            content = content + (time - lastTime);
        }
        lastTime = time;
        Log.i(getPrefix(), content);
    }

    /**
     * Send a VERBOSE log message,黑色
     */
    public static void v(String content) {
        if (allow) {
            Log.v(getPrefix(), content);
        }
    }

    /**
     * Send a WARN log message,黄色
     */
    public static void w(String content) {
        if (allow) {
            Log.w(getPrefix(), content);
        }
    }

    public static void e(String content, Throwable tr) {
        if (allow) {
            Log.e(getPrefix(), content, tr);
        }
    }

    public static void e(Throwable tr) {
        if (allow) {
            Log.e(getPrefix(), tr.getMessage());
        }
    }


    /* ***************** super log 带行号并且点击可跳转的log **************************/

    /**
     * Send an INFO log message,绿色
     */
    public static void printI(String log) {
        if (allow && isSuperLog) {
            Log.i(TAG, log + "-->" + callMethodAndLine());
        }
    }

    /**
     * Send a DEBUG log message,蓝色
     */
    public static void printD(String log) {
        if (allow && isSuperLog) {
            Log.d(TAG, log + "-->" + callMethodAndLine());
        }
    }

    /**
     * Send an ERROR log message,红色
     */
    public static void printE(String log) {
        if (allow && isSuperLog) {
            Log.e(TAG, log + "-->" + callMethodAndLine());
        }
    }

    /**
     * Send a VERBOSE log message,黑色/白色
     */
    public static void printV(String log) {
        if (allow && isSuperLog) {
            Log.v("TAG", log + "-->" + callMethodAndLine());
        }
    }

    /**
     * Send a WARN log message,黄色
     */
    public static void printW(String log) {
        if (allow && isSuperLog) {
            Log.w(TAG, log + "-->" + callMethodAndLine());
        }
    }

//    public static void printStackTrace() {
//        Stream.of(Thread.currentThread().getStackTrace()).forEach(threadEntry -> {
//            Log.e(getPrefix(), threadEntry.getClassName() + " " + threadEntry.getMethodName());
//        });
//    }

    private static String callMethodAndLine() {
        StringBuilder result = new StringBuilder("at ");
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        result.append(thisMethodStack.getClassName())
                .append(".")
                .append(thisMethodStack.getMethodName())
                .append("(")
                .append(thisMethodStack.getFileName())
                .append(":")
                .append(thisMethodStack.getLineNumber())
                .append(")  ");
        return result.toString();
    }

    public static void xml(String xml) {
        if (TextUtils.isEmpty(xml)) {
            Log.d(TAG, "Empty/Null xml content");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            Log.d(TAG, xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            Log.d(TAG, e.getCause().getMessage() + "\n" + xml);
        }
    }


    /**
     * Formats the json content and print it
     * 格式JSON内容和打印
     *
     * @param json the json content
     */
    public static void json(String json) {
        if (TextUtils.isEmpty(json)) {
            Log.d(TAG, "Empty/Null json content");
            return;
        }
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                Log.d(TAG, message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                Log.d(TAG, message);
            }
        } catch (JSONException e) {
            Log.d(TAG, e.getCause().getMessage() + "\n" + json);
        }
    }

}
