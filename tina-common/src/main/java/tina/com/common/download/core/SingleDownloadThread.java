package tina.com.common.download.core;

import android.os.Process;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import tina.com.common.download.DownloadException;
import tina.com.common.download.entity.DownloadInfo;
import tina.com.common.download.entity.DownloadStatus;
import tina.com.common.download.entity.ThreadInfo;
import tina.com.common.download.utils.Constants;
import tina.com.common.download.utils.IOCloseUtils;

/**
 * @author yxc
 * @date 2018/8/6
 */
public class SingleDownloadThread extends DownloadThread {

    public SingleDownloadThread(DownloadInfo downloadInfo, ThreadInfo threadInfo, DownloadThread.OnDownloadListener downloadListener) {
        super(downloadInfo, threadInfo, downloadListener);
    }
    @Override
    protected int getResponseCode() {
        return HttpURLConnection.HTTP_OK;
    }

    @Override
    protected void newOrUpdate(ThreadInfo info) {
        // needn't Override this
    }

    @Override
    protected Map<String, String> getHttpHeaders(ThreadInfo info) {
        // simply return null
        return null;
    }

    @Override
    protected RandomAccessFile getFile(File dir, String name, long offset) throws IOException {
        File file = new File(dir, name);
        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
        raf.seek(0);
        return raf;
    }

}
