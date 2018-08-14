package tina.com.common.http.net.base;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import tina.com.common.http.net.exception.ApiException;
import tina.com.common.http.net.interfaces.ISubscriber;


/**
 * Created by Allen on 2017/5/3.
 *
 * @author Allen
 *         <p>
 *         基类BaseObserver
 */

public abstract class BaseObserver<T> implements Observer<T>, ISubscriber<T> {

    /**
     * 是否隐藏toast
     *
     * @return
     */
    protected boolean isHideToast() {
        return false;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        doOnSubscribe(d);
    }

    @Override
    public void onNext(@NonNull T t) {
        doOnNext(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        String error = ApiException.handleException(e).getMessage();
        setError(error);
    }


    @Override
    public void onComplete() {
        doOnCompleted();
    }


    private void setError(String errorMsg) {
        doOnError(errorMsg);
    }

}
