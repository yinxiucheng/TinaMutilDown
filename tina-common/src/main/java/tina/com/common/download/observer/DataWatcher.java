package tina.com.common.download.observer;


import java.util.Observable;
import java.util.Observer;

import tina.com.common.download.entity.DownloadInfo;

/**
 * @author yxc
 * @date 2018/8/1
 */
public abstract class DataWatcher implements Observer {

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof DownloadInfo){
            notifyObserver((DownloadInfo) data);
        }
    }

    public abstract void notifyObserver(DownloadInfo data);

}
