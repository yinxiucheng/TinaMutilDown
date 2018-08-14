package tina.com.common.download.entity;

/**
 * @author yxc
 * @date 2018/8/1
 */
public class DownloadStatus {
    public static final int IDLE = 0;
    public static final int WAITING = 1;

    public static final int STARTED = 2;
    public static final int CONNECTING = 3;
    public static final int CONNECTED = 4;
    public static final int DOWNLOADING = 5;
    public static final int PAUSED = 6;
    public static final int CANCELED = 7;
    public static final int COMPLETED = 8;

    public static final int INSTALLED = 9;
    public static final int FAILED = 10;

    public static final int STATUS_STORAGE_NOT_ENOUGH = 11;


    //获取当前状态对应 下一个status的 文案
    public static  String getNextStausText(int status){
        if (status == DownloadStatus.PAUSED){
            return "resume";
        }else if (status == DownloadStatus.DOWNLOADING){
            return "pause";
        }else if (status == DownloadStatus.IDLE){
            return "download";
        }else if (status == DownloadStatus.WAITING){
            return "waiting";
        }else if (status == DownloadStatus.COMPLETED){
            return "install";
        }else if (status == DownloadStatus.INSTALLED){
            return "open";
        }else if (status == DownloadStatus.FAILED){
            return "error";
        }else {
            return "download";
        }
    }


    //获取
    public static String getStausText(int status){
        if (status == DownloadStatus.PAUSED){
            return "pause";
        }else if (status == DownloadStatus.DOWNLOADING){
            return "downloading";
        }else if (status == DownloadStatus.IDLE){
            return "idle";
        }else if (status == DownloadStatus.WAITING){
            return "waiting";
        }else if (status == DownloadStatus.COMPLETED){
            return "completed";
        }else if (status == DownloadStatus.INSTALLED){
            return "installed";
        }else if (status == DownloadStatus.FAILED){
            return "error";
        }else {
            return "download";
        }
    }
}
