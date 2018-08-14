package tina.com.common.downloadback;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import tina.com.common.download.utils.NetworkUtil;
import okhttp3.ResponseBody;
import tina.com.common.downloadback.buffer.DownInfo;
import tina.com.common.downloadback.buffer.DownInfoDbUtil;
import tina.com.common.downloadback.buffer.DownState;
import tina.com.common.downloadback.common.NetProgressListener;
import tina.com.common.downloadback.common.RetryWhenNetwork;

/**
 * @author: 小民
 * @date: 2017-06-07
 * @time: 15:54
 * @开源地址: https://github.com/2745329043/XDownloadDemo
 * @说明: 下载工具类
 */
public class RetrofitDownloadManager implements NetProgressListener, DownResultListenner {
    //下载状态发生改变时，回调
    private DownResultListenner mDownResultListenner;
    //队列
    private LinkedHashMap<DownInfo, DownloadSubscriber> mProgressSubscriberHashMap = new LinkedHashMap<>();

    /* 构造 */
    public RetrofitDownloadManager(DownResultListenner downResultListenner) {
        //监听
        mDownResultListenner = downResultListenner;
        DownloadRetrofitClient.client().addNetProgressListener(this);
        //输出日志
        DownloadRetrofitClient.log("RetrofitDownloadManager：创建对象");
    }

    /**
     * 暂停下载
     * @param downInfo 操作的对象类
     * @param isNextDownload 暂停后，是否检测开启下一个队列
     */
    private void pause(DownInfo downInfo,boolean isNextDownload) {
        DownloadSubscriber subscriber = mProgressSubscriberHashMap.get(downInfo);
        //等待中的时候，它是没有观察者的
        if(subscriber == null){
            return;
        }
        subscriber.dispose();
        //刷新状态
        updateState(downInfo, DownState.PAUSE,isNextDownload);
        //输出日志
        DownloadRetrofitClient.log("暂停：并保存数据");
    }

    /**
     * 开始下载
     * @param downInfo   操作的类
     */
    private void start(final DownInfo downInfo) {
        //队列已满，进入等待状态
        if(DownloadRetrofitClient.client().getCurrentDownloadCount() >= DownloadRetrofitClient.client().getMaxDownloadCount()){
            DownloadRetrofitClient.log("队列已满，进入等待.");
            updateState(downInfo,DownState.WAIT);
            //加入等待队列
            mProgressSubscriberHashMap.put(downInfo,null);
            return;
        }
        //新增队列
        DownloadRetrofitClient.client().addCurrentDownloadCount();
        //观察者
        DownloadSubscriber<DownInfo> downloadSubscriber = new DownloadSubscriber<>(downInfo,this);
        //开始下载
        DownloadRetrofitClient.getDownService()
                .download("bytes=" + downInfo.getReadLength() + "-", downInfo.getUrl())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .retryWhen(new RetryWhenNetwork())
                .map(new Function<ResponseBody, DownInfo>() {
                    @Override
                    public DownInfo apply(ResponseBody responseBody) throws Exception {
                        NetworkUtil.getInstance().writeCache(responseBody, new File(downInfo.getSavePath()), downInfo);
                        return downInfo;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribe(downloadSubscriber);
        //记录
        mProgressSubscriberHashMap.put(downInfo, downloadSubscriber);
        //输出log
        DownloadRetrofitClient.log("开始下载："+ downInfo.getUrl() + " 起始点：" + downInfo.getReadLength());
    }

    /**
     * 让一个等待的 队列先开始，暂停掉某一条
     */
    private void wait(DownInfo downInfo){
        if(DownloadRetrofitClient.client().getCurrentDownloadCount() >= DownloadRetrofitClient.client().getMaxDownloadCount()){
            Set<DownInfo> downInfos = mProgressSubscriberHashMap.keySet();
            Iterator<DownInfo> iterator = downInfos.iterator();
            while (iterator.hasNext()){
                DownInfo next = iterator.next();
                if(next.getState() == DownState.DOWN){
                    pause(next,false);
                    updateState(next,DownState.WAIT,false);
                    break;
                }
            }
        }
        start(downInfo);
    }
    /**
     * 下载
     */
    public void down(DownInfo downInfo) {
        DownState state = downInfo.getState();
        switch (state) {
            case NORMAL:
                start(downInfo);
                break;
            case WAIT:
                wait(downInfo);
                break;
            case DOWN:
                pause(downInfo,true);
                break;
            case PAUSE:
            case STOP:
                start(downInfo);
                break;
            case ERROR:
                //失败重新下载
                downInfo.setReadLength(0);
                downInfo.setCountLength(0);
                start(downInfo);
                break;
            case FINISH:
                //安装apk
                if (downInfo.getUrl().contains("apk")) {
                    String path = downInfo.getSavePath();
                    NetworkUtil.getInstance().installApk(path);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 更新进度
     */
    @Override
    public void update(String url, long readCount, long totalCount, boolean done) {
        DownInfo downInfo = queryDownInfoBy(url);
        if (downInfo != null) {
            //之前的进度
            int pretProgress = (int) (downInfo.getReadLength() * 1.0 / downInfo.getCountLength() * 100);
            //保存 readCount/setCountLength
            if (downInfo.getCountLength() > totalCount) {
                readCount = downInfo.getCountLength() - totalCount + readCount;
            } else {
                downInfo.setCountLength(totalCount);
            }
            downInfo.setReadLength(readCount);
            //进度如果没有改变。就不通知UI刷新了
            int progress = downInfo.getProgress();
            if (pretProgress != progress) {
                //刷新进度
                updateProgress(downInfo, progress);
            }
        }
    }

    /**
     * 根据URL 查找下载对象
     */
    private DownInfo queryDownInfoBy(String url) {
        Set<DownInfo> downInfos = mProgressSubscriberHashMap.keySet();
        Iterator<DownInfo> iterator = downInfos.iterator();
        while (iterator.hasNext()) {
            DownInfo next = iterator.next();
            if (next.getUrl().equals(url)) {
                return next;
            }
        }
        return null;
    }

    /**
     * 初始化DownInfo，主要为了缓存 与 避免用于删除。APP 强退等，数据问题
     *
     * @param downUrl  下载地址   不传  默认放在下载目录 donload
     * @return 下载对象
     */
    public DownInfo createDownInfo(String downUrl) {
        String savePath = NetworkUtil.getInstance().getDownloadDirectory().getAbsolutePath();
        return createDownInfo(downUrl, savePath, null);
    }

    /**
     * 初始化DownInfo，主要为了缓存 与 避免用于删除。APP 强退等，数据问题
     *
     * @param downUrl  下载地址
     * @param savePath 保存地址
     * @return 下载对象
     */
    public DownInfo createDownInfo(String downUrl, String savePath) {
        return createDownInfo(downUrl, savePath, null);
    }

    /**
     * 初始化DownInfo，主要为了缓存 与 避免用于删除。APP 强退等，数据问题
     *
     * @param downUrl  下载地址
     * @param savePath 保存地址
     * @param fileName 文件名 为空默认 下载文件名
     * @return 下载对象
     */
    public DownInfo createDownInfo(String downUrl, String savePath, String fileName) {
        DownInfo downInfo = DownInfoDbUtil.getInstance().query(downUrl);
        //文件被删除
        if (downInfo != null) {
            File file = new File(downInfo.getSavePath());
            if (!file.exists()) {
                DownInfoDbUtil.getInstance().delete(downInfo);
                downInfo = null;
            }
        }
        //正常流程
        if (downInfo == null) {
            downInfo = new DownInfo(downUrl, savePath, fileName);
        }
        //应用被强关的时候
        if (downInfo.getState() == DownState.DOWN) {
            downInfo.setDownState(DownState.PAUSE.getState());
        }
        //只要不是 完成状态，都记录起来。可以开启自动下载
        if (downInfo.getState() != DownState.FINISH){
            mProgressSubscriberHashMap.put(downInfo,null);
        }
        //设置监听
        downInfo.setListener(this);
        return downInfo;
    }

    /**
     * 界面退出的时候，记得销毁对象，不销毁。后台会静默下载
     */
    public void destory() {
        DownloadRetrofitClient.client().removeNetProgressListener(this);
        //暂停
        Set<DownInfo> downInfos = mProgressSubscriberHashMap.keySet();
        Iterator<DownInfo> iterator = downInfos.iterator();
        while (iterator.hasNext()) {
            DownInfo next = iterator.next();
            if(next.getState() == DownState.DOWN){
                pause(next,false);
            }
        }
        mProgressSubscriberHashMap.clear();
        mProgressSubscriberHashMap = null;
        DownloadRetrofitClient.log("RetrofitDownloadManager -》 gc");
    }

    /** 进度发生改变的时候 */
    @Override
    public void updateProgress(final DownInfo info, final int progress) {
        //输出日志
        DownloadRetrofitClient.log("updateProgress -》" + progress + " %");
        //更新数据库
        DownInfoDbUtil.getInstance().update(info);
        // 回调 主线程
        Observable.just(info)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownInfo>() {
                    @Override
                    public void accept(DownInfo downInfo) throws Exception {
                        mDownResultListenner.updateProgress(downInfo, progress);
                    }
                });

    }

    /** 下载状态发生改变的时候 */
    @Override
    public void updateState(DownInfo info, DownState state) {
        updateState(info,state,true);
    }

    /**
     * 下载状态发生改变的时候
     * @param info             修改的对象
     * @param state            新的状态
     * @param isNextDownload   是否开启下一个队列
     */
    public void updateState(DownInfo info, final DownState state, boolean isNextDownload) {
        DownloadRetrofitClient.log(String.format("下载地址:%s\n状态：%d",info.getUrl(),state.getState()));
        info.setDownState(state.getState());
        //更新下载的数据
        DownInfoDbUtil.getInstance().update(info);
        //查找队列是否需要开启
        switch (state){
            //暂停，停止，错误，完成，开启下一个下载
            case PAUSE:
            case STOP:
            case ERROR:
            case FINISH:
                //删除队列
                DownloadRetrofitClient.client().subCurrentDownloadCount();
                //下一个任务
                if(isNextDownload){
                    nextDownload();
                }
                break;
            default:
                break;
        }
        // 回调 主线程
        Observable.just(info)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownInfo>() {
                    @Override
                    public void accept(DownInfo downInfo) throws Exception {
                        mDownResultListenner.updateState(downInfo, state);
                    }
                });
    }

    /** 查找 为 WAIT 状态的队列 并开启他，只开一个 */
    private void nextDownload(){
        Set<DownInfo> downInfos = mProgressSubscriberHashMap.keySet();
        Iterator<DownInfo> iterator = downInfos.iterator();
        while (iterator.hasNext()) {
            DownInfo next = iterator.next();
            if (next.getState() == DownState.WAIT) {
                //如果处于等待。并且队列没满
                if (DownloadRetrofitClient.client().getCurrentDownloadCount() < DownloadRetrofitClient.client().getMaxDownloadCount()) {
                    start(next);
                } else {
                    break;
                }
            }
        }
    }

    /** 开始全部下载,一般用户app重新打开时 */
    public void downloadAll(){
        Set<DownInfo> downInfos = mProgressSubscriberHashMap.keySet();
        Iterator<DownInfo> iterator = downInfos.iterator();
        while (iterator.hasNext()) {
            DownInfo next = iterator.next();
            //四种状态下，自动重启队列 WAIT（等待） PAUSED（暂停） STOP（停止） ERROR（错误）
            if (next.getState() == DownState.WAIT || next.getState() == DownState.PAUSE || next.getState() == DownState.STOP || next.getState() == DownState.ERROR) {
                //如果处于等待。并且队列没满
                if (DownloadRetrofitClient.client().getCurrentDownloadCount() < DownloadRetrofitClient.client().getMaxDownloadCount()) {
                    down(next);
                } else {
                    break;
                }
            }
        }
    }
}
