package tina.com.common.download.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ThreadInfo {

    @Id(autoincrement = true)
    private Long _id;

    private int index;
    private String tag;
    private String url;
    private long start;
    private long end;
    private long finished;
    private int status;

    public ThreadInfo(int index, String tag, String url, long start,
                      long end, long finished, int status) {
        this.index = index;
        this.tag = tag;
        this.url = url;
        this.start = start;
        this.end = end;
        this.finished = finished;
        this.status = status;
    }

    public ThreadInfo(int index, String tag, String url, int status) {
        this.index = index;
        this.tag = tag;
        this.url = url;
        this.status = status;
    }

    @Generated(hash = 996795298)
    public ThreadInfo(Long _id, int index, String tag, String url, long start,
            long end, long finished, int status) {
        this._id = _id;
        this.index = index;
        this.tag = tag;
        this.url = url;
        this.start = start;
        this.end = end;
        this.finished = finished;
        this.status = status;
    }

    @Generated(hash = 930225280)
    public ThreadInfo() {
    }


    @Override
    public String toString() {
        return "ThreadInfo{" +
                "index =" + index +
                ", tag='" + tag + '\'' +
                ", url='" + url + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", finished=" + finished +
                '}';
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getStart() {
        return this.start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return this.end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getFinished() {
        return this.finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
