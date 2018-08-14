package tina.com.common.downloadback.common;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author: 小民
 * @date: 2017-06-06
 * @time: 20:26
 * @开源地址: https://github.com/2745329043/XDownloadDemo
 * @说明: 自定义进度条 Body
 */
public class NetProgressResponseBody extends ResponseBody {
    private String url;
    private ResponseBody responseBody;
    private NetProgressListener progressListener;
    private BufferedSource bufferedSource;

    public NetProgressResponseBody(String url, ResponseBody responseBody, NetProgressListener progressListener) {
        this.url = url;
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }


    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (progressListener != null) {
                    progressListener.update(url,totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                }
                return bytesRead;
            }
        };
    }


    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }
}
