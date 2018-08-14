package tina.com.common.downloadback.buffer;

import android.content.Context;

import com.live_common.download.gen.BufferResulteDao;
import com.live_common.download.gen.DaoMaster;
import com.live_common.download.gen.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import tina.com.common.downloadback.buffer.BufferResulte;

/**
 * @author: 小民
 * @date: 2017-06-06
 * @time: 11:16
 * @开源地址: https://github.com/2745329043/XDownloadDemo
 * @说明: 用于网络请求过程的  缓存
 */
public class BufferDbUtil {
    /* 单例 */
    private static BufferDbUtil db;
    //GreenDao操作
    private final DaoMaster.DevOpenHelper mOpenHelper;

    private BufferDbUtil(Context context, String dbName){
        mOpenHelper = new DaoMaster.DevOpenHelper(context, dbName);
    }

    /** 初始化 */
    public static void init(Context context, String dbName){
        db = new BufferDbUtil(context,dbName);
    }

    /** 单例 */
    public static BufferDbUtil getInstance(){
        return db;
    }

    /** 增 */
    public void insert(BufferResulte info){
        DaoMaster daoMaster = new DaoMaster(mOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        BufferResulteDao downInfoDao = daoSession.getBufferResulteDao();
        downInfoDao.insert(info);
    }

    /** 删 */
    public void delete(BufferResulte info){
        DaoMaster daoMaster = new DaoMaster(mOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        BufferResulteDao downInfoDao = daoSession.getBufferResulteDao();
        downInfoDao.delete(info);
    }

    /** 改 */
    public void update(BufferResulte info){
        DaoMaster daoMaster = new DaoMaster(mOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        BufferResulteDao downInfoDao = daoSession.getBufferResulteDao();
        downInfoDao.update(info);
    }

    /** 查 */
    public BufferResulte query(String url){
        DaoMaster daoMaster = new DaoMaster(mOpenHelper.getReadableDb());
        DaoSession daoSession = daoMaster.newSession();
        BufferResulteDao downInfoDao = daoSession.getBufferResulteDao();
        QueryBuilder<BufferResulte> qb = downInfoDao.queryBuilder();
        qb.where(BufferResulteDao.Properties.Url.eq(url));
        List<BufferResulte> list = qb.list();
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    /** 查全部 */
    public List<BufferResulte> queryAll() {
        DaoMaster daoMaster = new DaoMaster(mOpenHelper.getReadableDb());
        DaoSession daoSession = daoMaster.newSession();
        BufferResulteDao downInfoDao = daoSession.getBufferResulteDao();
        QueryBuilder<BufferResulte> qb = downInfoDao.queryBuilder();
        return qb.list();
    }


    /**
     * 更新缓存数据
     * @param json 缓存的Json
     */
    public void updateResulteBy(String tag, String json) {
        BufferResulte resulte = BufferDbUtil.getInstance().query(tag);
        long time = System.currentTimeMillis();
        if (resulte == null) {
            resulte = new BufferResulte(null, tag, json, time);
            BufferDbUtil.getInstance().insert(resulte);
        } else {
            resulte.setResulte(json);
            resulte.setTime(time);
            BufferDbUtil.getInstance().update(resulte);
        }
    }
}