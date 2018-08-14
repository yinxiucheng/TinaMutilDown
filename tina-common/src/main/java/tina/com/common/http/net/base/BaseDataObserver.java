package tina.com.common.http.net.base;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import tina.com.common.http.net.bean.BaseData;
import tina.com.common.http.net.exception.ApiException;
import tina.com.common.http.net.interfaces.IDataSubscriber;

/**
 * Created by Allen on 2017/10/27.
 *
 * @author Allen
 *         <p>
 *         基类BaseObserver使用BaseData
 */

public abstract class BaseDataObserver<T> implements Observer<BaseData<T>>, IDataSubscriber<T> {

    /**
     * 是否隐藏toast
     *
     * @return
     */
    protected boolean isHideToast() {
        return false;
    }

    @Override
    public void onSubscribe(Disposable d) {
        doOnSubscribe(d);
    }

    @Override
    public void onNext(BaseData<T> baseData) {
        doOnNext(baseData);
    }

    @Override
    public void onError(Throwable e) {
        ApiException apiException = ApiException.handleException(e);
        String error = apiException.getMessage();
        int errorCode = apiException.getCode();
        setError(errorCode, error);
    }

    @Override
    public void onComplete() {
        doOnCompleted();
    }


    private void setError(int errorCode, String errorMsg) {
        doOnError(errorCode, errorMsg);
    }

}
