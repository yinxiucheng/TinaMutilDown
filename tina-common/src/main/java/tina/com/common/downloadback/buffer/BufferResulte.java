package tina.com.common.downloadback.buffer;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author: 小民
 * @date: 2017-06-06
 * @time: 11:30
 * @开源地址: https://github.com/2745329043/XDownloadDemo
 * @说明: 网络请求  缓存数据
 */
@Entity
public class BufferResulte {
    @Id(autoincrement = true)
    private Long id;
    /*url*/
    private String url;
    /*返回结果*/
    private String resulte;
    /*时间*/
    private long time;
    @Generated(hash = 1964623798)
    public BufferResulte(Long id, String url, String resulte, long time) {
        this.id = id;
        this.url = url;
        this.resulte = resulte;
        this.time = time;
    }
    @Generated(hash = 1186692049)
    public BufferResulte() {
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
    public String getResulte() {
        return this.resulte;
    }
    public void setResulte(String resulte) {
        this.resulte = resulte;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }

}
