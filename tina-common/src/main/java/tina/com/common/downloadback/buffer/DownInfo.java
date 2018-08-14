package tina.com.common.downloadback.buffer;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Random;

import tina.com.common.downloadback.DownResultListenner;


/**
 * @author: 小民
 * @date: 2017-06-06
 * @time: 14:21
 * @开源地址: https://github.com/2745329043/XDownloadDemo
 * @说明: 下载请求基础类
 */
@Entity
public class DownInfo {
    @Id(autoincrement = true)
    private Long id;
    /*url*/
    private String url;
    /*存储位置*/
    private String savePath;
    /*下载长度*/
    private long readLength;
    /*文件总长度*/
    private long countLength;
    /* 下载状态 */
    private int downState;
    /*回调监听*/
    @Transient
    private DownResultListenner listener;

    public DownInfo(String url, String savePath, String fileName){
        //为空，以下载地址文件名为标准
        if(fileName == null){
            fileName = url.substring(url.lastIndexOf("/"));
            if(!fileName.contains(".")){
                fileName = String.valueOf(new Random().nextInt(Integer.MAX_VALUE));
            }
        }
        this.url = url;
        this.savePath = savePath + fileName;
    }



    @Generated(hash = 1812925848)
    public DownInfo(Long id, String url, String savePath, long readLength,
            long countLength, int downState) {
        this.id = id;
        this.url = url;
        this.savePath = savePath;
        this.readLength = readLength;
        this.countLength = countLength;
        this.downState = downState;
    }



    @Generated(hash = 928324469)
    public DownInfo() {
    }



    public DownResultListenner getListener() {
        return listener;
    }

    public void setListener(DownResultListenner listener) {
        this.listener = listener;
    }



    public Long getId() {
        return this.id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public String getUrl() {
        return this.url;
    }



    public void setUrl(String url) {
        this.url = url;
    }



    public String getSavePath() {
        return this.savePath;
    }



    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }



    public long getReadLength() {
        return this.readLength;
    }



    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }



    public long getCountLength() {
        return this.countLength;
    }



    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }



    public int getDownState() {
        return this.downState;
    }



    public void setDownState(int downState) {
        this.downState = downState;
    }

    /** 进度条  max:100 */
    public int getProgress(){
        int progress = (int) (getReadLength() * 1.0 / getCountLength() * 100);
        return progress;
    }
    /** 下载状态 */
    public DownState getState() {
        switch (getDownState()){
            case 0:
                return DownState.NORMAL;
            case 1:
                return DownState.WAIT;
            case 2:
                return DownState.DOWN;
            case 3:
                return DownState.PAUSE;
            case 4:
                return DownState.STOP;
            case 5:
                return DownState.ERROR;
            case 6:
            default:
                return DownState.FINISH;
        }
    }

    /** 对应下载状态，对应因该显示什么文本内容 */
    public String getStateText(){
        switch (getDownState()){
            case 0:
                return "下载";
            case 1:
                return "等待";
            case 2:
                return "暂停";
            case 3:
                return "继续";
            case 4:
                return "停止";
            case 5:
                return "重试";
            case 6:
            default:
                return "安装";
        }
    }

}
