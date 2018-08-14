package tina.com.live_base.utils;

import android.os.SystemClock;

public class BaseUIUtil {

    private static long processFinishTime;

    public static synchronized boolean isProcessing() {
        return isProcessing(150);
    }

    /**
     * 是否处于执行任务状态，若不是，则设接下来[minTime]ms 为执行任务状态
     *
     * @param minTime 任务执行时长
     * @return 是否处于执行任务状态
     */
    public static synchronized boolean isProcessing(final long minTime) {
        long newTime = newEffectiveTime(minTime, processFinishTime);
        if (newTime == processFinishTime) {
            return true;
        } else {
            processFinishTime = newTime;
            return false;
        }
    }

    /**
     * 以上次任务完成时间戳为基准，计算下次任务执行到的时间戳，若上次时间戳未到，则直接返回上次时间戳
     *
     * @param minTime  任务执行时长
     * @param lastTime 上次完成时间戳，以包括休眠时间的系统运行时长计时
     * @return 下次任务执行到的时间戳
     */
    private static synchronized long newEffectiveTime(final long minTime, long lastTime) {
        if (SystemClock.elapsedRealtime() >= lastTime) {
            return SystemClock.elapsedRealtime() + minTime;
        }
        return lastTime;
    }


}
