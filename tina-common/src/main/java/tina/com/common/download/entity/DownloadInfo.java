package tina.com.common.download.entity;

import com.live_common.download.gen.DaoSession;
import com.live_common.download.gen.DownloadInfoDao;
import com.live_common.download.gen.ThreadInfoDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import tina.com.common.download.utils.DownloadConfig;

/**
 * @author yxc
 * @date 2018/8/1
 */
@Entity
public class DownloadInfo implements Serializable{

    static final long serialVersionUID = 42L;
    @Id
    public String tag;

    public int index;

    public String fileName;

    public String url;

    public long finish;

    public long length;

    public int status;

    public int progress;

    public String name;

    public boolean acceptRanges;

    public String image;

    public String packageName;

    public String versionCode;

    @ToMany(referencedJoinProperty="tag")
    public List<ThreadInfo> threadInfoList;
    
    public DownloadInfo(){}

    public DownloadInfo(int index, String name, String fileName, String iamge, String url) {
        this.index = index;
        this.name = name;
        this.fileName = fileName;
        this.image = iamge;
        this.url = url;
    }

    @Generated(hash = 146078028)
    public DownloadInfo(String tag, int index, String fileName, String url, long finish, long length, int status,
            int progress, String name, boolean acceptRanges, String image, String packageName, String versionCode) {
        this.tag = tag;
        this.index = index;
        this.fileName = fileName;
        this.url = url;
        this.finish = finish;
        this.length = length;
        this.status = status;
        this.progress = progress;
        this.name = name;
        this.acceptRanges = acceptRanges;
        this.image = image;
        this.packageName = packageName;
        this.versionCode = versionCode;
    }

    @Override
    public boolean equals(Object o) {
        return this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return fileName + ":" + getDownloadPerSize() + "::" + DownloadStatus.getStausText(status) + ":" + progress ;
    }
    private static final DecimalFormat DF = new DecimalFormat("0.00");
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1465593784)
    private transient DownloadInfoDao myDao;
    public String getDownloadPerSize() {
        return DF.format((float) finish / (1024 * 1024)) + "M/" + DF.format((float) length / (1024 * 1024)) + "M";
    }

    public void reset() {
        finish = 0;
        progress = 0;
        File file = DownloadConfig.getInstance().getDownloadFile(url);
        if (file.exists()){
            file.delete();
        }
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getFinish() {
        return this.finish;
    }

    public void setFinish(long finish) {
        this.finish = finish;
    }

    public long getLength() {
        return this.length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getAcceptRanges() {
        return this.acceptRanges;
    }

    public void setAcceptRanges(boolean acceptRanges) {
        this.acceptRanges = acceptRanges;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionCode() {
        return this.versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1847536389)
    public List<ThreadInfo> getThreadInfoList() {
        if (threadInfoList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ThreadInfoDao targetDao = daoSession.getThreadInfoDao();
            List<ThreadInfo> threadInfoListNew = targetDao._queryDownloadInfo_ThreadInfoList(tag);
            synchronized (this) {
                if (threadInfoList == null) {
                    threadInfoList = threadInfoListNew;
                }
            }
        }
        return threadInfoList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 153469034)
    public synchronized void resetThreadInfoList() {
        threadInfoList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 17038220)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDownloadInfoDao() : null;
    }

}
