package com.live_common.download.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import tina.com.common.download.entity.DownloadInfo;
import tina.com.common.download.entity.ThreadInfo;

import com.live_common.download.gen.DownloadInfoDao;
import com.live_common.download.gen.ThreadInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig downloadInfoDaoConfig;
    private final DaoConfig threadInfoDaoConfig;

    private final DownloadInfoDao downloadInfoDao;
    private final ThreadInfoDao threadInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        downloadInfoDaoConfig = daoConfigMap.get(DownloadInfoDao.class).clone();
        downloadInfoDaoConfig.initIdentityScope(type);

        threadInfoDaoConfig = daoConfigMap.get(ThreadInfoDao.class).clone();
        threadInfoDaoConfig.initIdentityScope(type);

        downloadInfoDao = new DownloadInfoDao(downloadInfoDaoConfig, this);
        threadInfoDao = new ThreadInfoDao(threadInfoDaoConfig, this);

        registerDao(DownloadInfo.class, downloadInfoDao);
        registerDao(ThreadInfo.class, threadInfoDao);
    }
    
    public void clear() {
        downloadInfoDaoConfig.clearIdentityScope();
        threadInfoDaoConfig.clearIdentityScope();
    }

    public DownloadInfoDao getDownloadInfoDao() {
        return downloadInfoDao;
    }

    public ThreadInfoDao getThreadInfoDao() {
        return threadInfoDao;
    }

}
