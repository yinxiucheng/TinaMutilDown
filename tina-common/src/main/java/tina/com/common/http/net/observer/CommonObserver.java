package tina.com.common.http.net.observer;


import android.app.Dialog;
import android.text.TextUtils;

import io.reactivex.disposables.Disposable;
import tina.com.common.http.net.RxHttpUtils;
import tina.com.common.http.net.base.BaseObserver;
import tina.com.common.http.utils.ToastUtil;

/**
 * Created by Allen on 2017/5/3.
 *
 * @author Allen
 * 通用的Observer
 * 用户可以根据自己需求自定义自己的类继承BaseObserver<T>即可
 */

public abstract class CommonObserver<T> extends BaseObserver<T> {


    private Dialog mProgressDialog;

    public CommonObserver() {
    }

    public CommonObserver(Dialog progressDialog) {
        mProgressDialog = progressDialog;
    }

    /**
     * 失败回调
     *
     * @param errorMsg
     */
    protected abstract void onError(String errorMsg);

    /**
     * 成功回调
     *
     * @param t
     */
    protected abstract void onSuccess(T t);


    @Override
    public void doOnSubscribe(Disposable d) {
        //RxHttpUtils.addDisposable(d);
        RxHttpUtils.addToCompositeDisposable(d);
    }

    @Override
    public void doOnError(String errorMsg) {
        dismissLoading();
        if (!isHideToast() && !TextUtils.isEmpty(errorMsg)) {
            ToastUtil.show(errorMsg);
        }
        onError(errorMsg);
    }

    @Override
    public void doOnNext(T t) {
        onSuccess(t);
    }

    @Override
    public void doOnCompleted() {
        dismissLoading();
    }

    /**
     * 隐藏loading对话框
     */
    private void dismissLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
