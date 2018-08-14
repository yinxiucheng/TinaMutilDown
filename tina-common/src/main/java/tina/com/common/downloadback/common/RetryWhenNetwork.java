package tina.com.common.downloadback.common;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * @author: 小民
 * @date: 2017-06-05
 * @time: 17:32
 * @开源地址: https://github.com/2745329043/XDownloadDemo
 * @说明: 网络失败时，用于重新连接
 */
public class RetryWhenNetwork implements Function<Observable<? extends Throwable>, Observable<?>> {
    //    retry次数
    private int count = 3;
    //    延迟多少秒后重试
    private long delay = 3000;
    //    叠加延迟
    private long increaseDelay = 3000;

    public RetryWhenNetwork() {

    }

    public RetryWhenNetwork(int count, long delay, long increaseDelay) {
        this.count = count;
        this.delay = delay;
        this.increaseDelay = increaseDelay;
    }


    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        return observable
                .zipWith(Observable.range(1, count + 1), new BiFunction<Throwable, Integer, Wrapper>() {
                    @Override
                    public Wrapper apply(Throwable throwable, Integer integer) throws Exception {
                        return new Wrapper(throwable, integer);
                    }

                }).flatMap(new Function<Wrapper, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Wrapper wrapper) throws Exception {
                        //如果超出重试次数也抛出错误，否则默认是会进入onCompleted
                        if ((wrapper.throwable instanceof ConnectException
                                || wrapper.throwable instanceof SocketTimeoutException
                                || wrapper.throwable instanceof TimeoutException)
                                && wrapper.index < count + 1) {
                            //如果你不需要叠加式尝试  请使用 return Observable.timer(increaseDelay,TimeUnit.MILLISECONDS);
                            return Observable.timer(delay + (wrapper.index - 1) * increaseDelay, TimeUnit.MILLISECONDS);

                        }
                        return Observable.error(wrapper.throwable);
                    }
                });
    }

    private class Wrapper {
        private int index;
        private Throwable throwable;

        public Wrapper(Throwable throwable, int index) {
            this.index = index;
            this.throwable = throwable;
        }
    }

}

