package tina.com.common.downloadback.buffer;

import android.content.Context;

import com.live_common.download.gen.DaoMaster;
import com.live_common.download.gen.DaoSession;
import com.live_common.download.gen.DownInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;



/**
 * @author: 小民
 * @date: 2017-06-08
 * @time: 15:29
 * @开源地址: https://github.com/2745329043/XDownloadDemo
 * @说明:
 */
public class DownInfoDbUtil {
    /* 单例 */
    private static DownInfoDbUtil db;
    //GreenDao操作
    private final DaoMaster.DevOpenHelper mOpenHelper;

    private DownInfoDbUtil(Context context, String dbName) {
        mOpenHelper = new DaoMaster.DevOpenHelper(context, dbName);
    }

    /**
     * 初始化
     */
    public static void init(Context context, String dbName) {
        db = new DownInfoDbUtil(context,dbName);
    }

    /**
     * 单例
     */
    public static DownInfoDbUtil getInstance() {
        return db;
    }

    /**
     * 增
     */
    public void insert(DownInfo info) {
        DaoMaster daoMaster = new DaoMaster(mOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        downInfoDao.insert(info);
    }

    /**
     * 删
     */
    public void delete(DownInfo info) {
        DaoMaster daoMaster = new DaoMaster(mOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        downInfoDao.delete(info);
    }

    /**
     * 改
     */
    public void update(DownInfo info) {
        DownInfo query = query(info.getUrl());
        if( query == null){
            insert(info);
        }else{
            DaoMaster daoMaster = new DaoMaster(mOpenHelper.getWritableDb());
            DaoSession daoSession = daoMaster.newSession();
            DownInfoDao downInfoDao = daoSession.getDownInfoDao();
            info.setId(query.getId());
            downInfoDao.update(info);
        }

    }

    /**
     * 查
     */
    public DownInfo query(String url) {
        DaoMaster daoMaster = new DaoMaster(mOpenHelper.getReadableDb());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        QueryBuilder<DownInfo> qb = downInfoDao.queryBuilder();
        qb.where(DownInfoDao.Properties.Url.eq(url));
        List<DownInfo> list = qb.list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * 查全部
     */
    public List<DownInfo> queryAll() {
        DaoMaster daoMaster = new DaoMaster(mOpenHelper.getReadableDb());
        DaoSession daoSession = daoMaster.newSession();
        DownInfoDao downInfoDao = daoSession.getDownInfoDao();
        QueryBuilder<DownInfo> qb = downInfoDao.queryBuilder();
        return qb.list();
    }
}
